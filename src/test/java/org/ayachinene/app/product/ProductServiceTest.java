package org.ayachinene.app.product;

import org.ayachinene.app.exception.ValidationException;
import org.ayachinene.app.product.creation.CreateProductInput;
import org.ayachinene.app.product.creation.CreatedProduct;
import org.ayachinene.app.product.domain.CategoryCode;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.repository.ProductRepository;
import org.ayachinene.app.product.repository.SkuRepository;
import org.ayachinene.app.service.Tx;
import org.ayachinene.app.stock.repository.StockRepository;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    @Test
    void validatesCreatesAndInitializesStock() throws Exception {
        var productRepository = mock(ProductRepository.class);
        var stockRepository = mock(StockRepository.class);
        var tx = transactionRunner();
        var productCode = ProductCode.generate();
        var skuCode = SkuCode.generate();
        when(productRepository.create(any())).thenReturn(new CreatedProduct(
                productCode,
                List.of(skuCode)
        ));
        var service = new ProductService(
                productRepository,
                mock(SkuRepository.class),
                stockRepository,
                tx
        );

        var result = service.createProduct(validInput());

        assertEquals(productCode, result);
        var inputCaptor = ArgumentCaptor.forClass(CreateProductInput.class);
        verify(productRepository).create(inputCaptor.capture());
        assertEquals("T-Shirt", inputCaptor.getValue().title());
        verify(stockRepository).initialize(List.of(skuCode));
    }

    @Test
    void validatesBeforeStartingTheTransaction() {
        var productRepository = mock(ProductRepository.class);
        var stockRepository = mock(StockRepository.class);
        var tx = mock(Tx.class);
        var service = new ProductService(
                productRepository,
                mock(SkuRepository.class),
                stockRepository,
                tx
        );
        var invalid = new CreateProductInput(
                "T-Shirt",
                null,
                "Cotton",
                new CategoryCode("TSHIRT"),
                null,
                List.of(),
                List.of(),
                List.of()
        );

        assertThrows(
                ValidationException.class,
                () -> service.createProduct(invalid)
        );
        verifyNoInteractions(productRepository, stockRepository, tx);
    }

    private static Tx transactionRunner() throws Exception {
        var tx = mock(Tx.class);
        doAnswer(invocation -> invocation
                .getArgument(0, Callable.class)
                .call()
        ).when(tx).run(org.mockito.ArgumentMatchers.<Callable<Object>>any());
        return tx;
    }

    private static CreateProductInput validInput() {
        return new CreateProductInput(
                " T-Shirt ",
                null,
                " Cotton ",
                new CategoryCode("TSHIRT"),
                UUID7s.generate(),
                List.of(),
                List.of(),
                List.of(new CreateProductInput.Sku(
                        null,
                        BigDecimal.ONE,
                        null,
                        List.of()
                ))
        );
    }
}

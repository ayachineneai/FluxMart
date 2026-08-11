package org.ayachinene.app.product;

import org.ayachinene.app.product.domain.CategoryCode;
import org.ayachinene.app.product.repository.ProductRepository;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.creation.CreateProductInput;
import org.ayachinene.app.product.creation.ProductCreation;
import org.ayachinene.app.product.creation.ProductDetailsInput;
import org.ayachinene.app.product.creation.SelectionInput;
import org.ayachinene.app.product.creation.SkuInput;
import org.ayachinene.app.product.creation.SpecificationInput;
import org.ayachinene.app.product.repository.SkuRepository;
import org.ayachinene.app.exception.ValidationException;
import org.ayachinene.app.service.Tx;
import org.ayachinene.app.stock.repository.StockRepository;
import org.ayachinene.shared.uuid7.UUID7s;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ProductServiceTest {

    @Test
    void createsAndSavesADraftProduct() {
        var productRepository = mock(ProductRepository.class);
        var transactionRunner = mock(Tx.class);
        doAnswer(invocation -> {
            invocation.getArgument(0, Runnable.class).run();
            return null;
        }).when(transactionRunner).run(org.mockito.ArgumentMatchers.any(Runnable.class));
        var service = new ProductService(
                productRepository,
                mock(SkuRepository.class),
                mock(StockRepository.class),
                transactionRunner
        );

        var productCode = service.createProduct(new CreateProductInput(
                new ProductDetailsInput(
                        "  纯棉 T 恤  ",
                        null,
                        "  100% 纯棉  ",
                        new CategoryCode("TSHIRT"),
                        UUID7s.generate(),
                        List.of()
                ),
                List.of(),
                List.of(new SkuInput(
                        null,
                        BigDecimal.ONE,
                        null,
                        List.of()
                ))
        ));

        var creationCaptor = ArgumentCaptor.forClass(ProductCreation.class);
        verify(productRepository).create(creationCaptor.capture());
        var savedProduct = creationCaptor.getValue().product();

        assertEquals(productCode, savedProduct.productCode());
        assertEquals(ProductStatus.DRAFT, savedProduct.status());
        assertEquals("纯棉 T 恤", savedProduct.title());
        verify(transactionRunner).run(org.mockito.ArgumentMatchers.any(Runnable.class));
    }

    @Test
    void validatesSpecificationsAndSkusBeforeStartingTheTransaction() {
        var productRepository = mock(ProductRepository.class);
        var skuRepository = mock(SkuRepository.class);
        var transactionRunner = mock(Tx.class);
        var service = new ProductService(
                productRepository,
                skuRepository,
                mock(StockRepository.class),
                transactionRunner
        );

        var input = new CreateProductInput(
                new ProductDetailsInput(
                        "纯棉 T 恤",
                        null,
                        "100% 纯棉",
                        new CategoryCode("TSHIRT"),
                        UUID7s.generate(),
                        List.of()
                ),
                List.of(new SpecificationInput("颜色", List.of("黑色"))),
                List.of(new SkuInput(
                        null,
                        BigDecimal.ONE,
                        null,
                        List.of(new SelectionInput("尺码", "M"))
                ))
        );

        assertThrows(ValidationException.class, () -> service.createProduct(input));
        org.mockito.Mockito.verifyNoInteractions(
                productRepository,
                skuRepository,
                transactionRunner
        );
    }

    @Test
    void savesProductSpecificationsAndSkusInOrder() {
        var productRepository = mock(ProductRepository.class);
        var stockRepository = mock(StockRepository.class);
        var transactionRunner = mock(Tx.class);
        doAnswer(invocation -> {
            invocation.getArgument(0, Runnable.class).run();
            return null;
        }).when(transactionRunner).run(org.mockito.ArgumentMatchers.any(Runnable.class));
        var service = new ProductService(
                productRepository,
                mock(SkuRepository.class),
                stockRepository,
                transactionRunner
        );

        var productCode = service.createProduct(new CreateProductInput(
                new ProductDetailsInput(
                        "纯棉 T 恤",
                        null,
                        "100% 纯棉",
                        new CategoryCode("TSHIRT"),
                        UUID7s.generate(),
                        List.of()
                ),
                List.of(new SpecificationInput("颜色", List.of("黑色"))),
                List.of(new SkuInput(
                        "TSHIRT-BLACK",
                        new BigDecimal("99.00"),
                        null,
                        List.of(new SelectionInput("颜色", "黑色"))
                ))
        ));

        var creationCaptor = ArgumentCaptor.forClass(ProductCreation.class);
        verify(productRepository).create(creationCaptor.capture());
        var creation = creationCaptor.getValue();
        assertEquals(productCode, creation.product().productCode());
        assertEquals("颜色", creation.specifications().getFirst().name());
        assertEquals("TSHIRT-BLACK", creation.skus().getFirst().merchantSkuCode());
        verify(stockRepository).initialize(List.of(
                creation.skus().getFirst().skuCode()
        ));
    }
}

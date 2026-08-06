package org.ayachinene.app.service.product;

import org.ayachinene.app.domain.file.FileResourceId;
import org.ayachinene.app.domain.product.CategoryCode;
import org.ayachinene.app.domain.product.Product;
import org.ayachinene.app.domain.product.ProductRepository;
import org.ayachinene.app.domain.product.ProductStatus;
import org.ayachinene.app.domain.product.creation.CreateProductInput;
import org.ayachinene.app.domain.product.creation.SelectionInput;
import org.ayachinene.app.domain.product.creation.SkuInput;
import org.ayachinene.app.domain.product.creation.SpecificationInput;
import org.ayachinene.app.domain.product.sku.SkuRepository;
import org.ayachinene.app.domain.product.specification.SpecificationRepository;
import org.ayachinene.app.exception.ValidationException;
import org.ayachinene.app.service.Tx;
import org.ayachinene.app.uuid7.UUID7s;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ProductServiceTest {

    @Test
    void createsAndSavesADraftProduct() {
        var productRepository = mock(ProductRepository.class);
        var specificationRepository = mock(SpecificationRepository.class);
        var skuRepository = mock(SkuRepository.class);
        var transactionRunner = mock(Tx.class);
        doAnswer(invocation -> {
            invocation.getArgument(0, Runnable.class).run();
            return null;
        }).when(transactionRunner).run(org.mockito.ArgumentMatchers.any(Runnable.class));
        var service = new ProductService(
                productRepository,
                specificationRepository,
                skuRepository,
                transactionRunner
        );

        var productCode = service.createProduct(new CreateProductInput(
                "  纯棉 T 恤  ",
                null,
                "  100% 纯棉  ",
                new CategoryCode("TSHIRT"),
                new FileResourceId(UUID7s.generate()),
                List.of()
        ));

        var productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).create(productCaptor.capture());
        var savedProduct = productCaptor.getValue();

        assertEquals(productCode, savedProduct.productCode());
        assertEquals(ProductStatus.DRAFT, savedProduct.status());
        assertEquals("纯棉 T 恤", savedProduct.title());
        verify(specificationRepository).create(productCode, List.of());
        verify(skuRepository).create(productCode, List.of());
        verify(transactionRunner).run(org.mockito.ArgumentMatchers.any(Runnable.class));
    }

    @Test
    void validatesSpecificationsAndSkusBeforeStartingTheTransaction() {
        var productRepository = mock(ProductRepository.class);
        var specificationRepository = mock(SpecificationRepository.class);
        var skuRepository = mock(SkuRepository.class);
        var transactionRunner = mock(Tx.class);
        var service = new ProductService(
                productRepository,
                specificationRepository,
                skuRepository,
                transactionRunner
        );

        var input = new CreateProductInput(
                "纯棉 T 恤",
                null,
                "100% 纯棉",
                new CategoryCode("TSHIRT"),
                new FileResourceId(UUID7s.generate()),
                List.of(),
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
                specificationRepository,
                skuRepository,
                transactionRunner
        );
    }

    @Test
    void savesProductSpecificationsAndSkusInOrder() {
        var productRepository = mock(ProductRepository.class);
        var specificationRepository = mock(SpecificationRepository.class);
        var skuRepository = mock(SkuRepository.class);
        var transactionRunner = mock(Tx.class);
        doAnswer(invocation -> {
            invocation.getArgument(0, Runnable.class).run();
            return null;
        }).when(transactionRunner).run(org.mockito.ArgumentMatchers.any(Runnable.class));
        var service = new ProductService(
                productRepository,
                specificationRepository,
                skuRepository,
                transactionRunner
        );

        var productCode = service.createProduct(new CreateProductInput(
                "纯棉 T 恤",
                null,
                "100% 纯棉",
                new CategoryCode("TSHIRT"),
                new FileResourceId(UUID7s.generate()),
                List.of(),
                List.of(new SpecificationInput("颜色", List.of("黑色"))),
                List.of(new SkuInput(
                        "TSHIRT-BLACK",
                        new BigDecimal("99.00"),
                        null,
                        List.of(new SelectionInput("颜色", "黑色"))
                ))
        ));

        var order = inOrder(productRepository, specificationRepository, skuRepository);
        order.verify(productRepository).create(org.mockito.ArgumentMatchers.argThat(
                product -> product.productCode().equals(productCode)
        ));
        order.verify(specificationRepository).create(
                org.mockito.ArgumentMatchers.eq(productCode),
                org.mockito.ArgumentMatchers.argThat(specifications ->
                        specifications.size() == 1
                                && specifications.getFirst().name().equals("颜色")
                )
        );
        order.verify(skuRepository).create(
                org.mockito.ArgumentMatchers.eq(productCode),
                org.mockito.ArgumentMatchers.argThat(skus ->
                        skus.size() == 1
                                && skus.getFirst().merchantSkuCode().equals("TSHIRT-BLACK")
                )
        );
    }
}

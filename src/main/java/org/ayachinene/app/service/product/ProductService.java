package org.ayachinene.app.service.product;

import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.domain.product.Products;
import org.ayachinene.app.domain.product.ProductRepository;
import org.ayachinene.app.domain.product.SkuSpecificationValidator;
import org.ayachinene.app.domain.product.ProductValidator;
import org.ayachinene.app.domain.product.creation.CreateProductInput;
import org.ayachinene.app.domain.product.creation.ProductCreation;
import org.ayachinene.app.domain.product.sku.SkuRepository;
import org.ayachinene.app.domain.product.sku.SkuValidator;
import org.ayachinene.app.domain.product.specification.SpecificationRepository;
import org.ayachinene.app.domain.product.specification.SpecificationValidator;
import org.ayachinene.app.service.Tx;
import org.ayachinene.app.uuid7.UUID7s;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SpecificationRepository specificationRepository;
    private final SkuRepository skuRepository;
    private final Tx tx;

    public ProductService(
            ProductRepository productRepository,
            SpecificationRepository specificationRepository,
            SkuRepository skuRepository,
            Tx tx
    ) {
        this.productRepository = productRepository;
        this.specificationRepository = specificationRepository;
        this.skuRepository = skuRepository;
        this.tx = tx;
    }

    public ProductCode createProduct(CreateProductInput input) {
        Objects.requireNonNull(input, "input must not be null");

        var validatedInput = validateCreateProductInput(input);
        var newProductCode = new ProductCode(UUID7s.generate());
        var creation = Products.mkProductCreation(newProductCode, validatedInput);

        tx.run(() -> saveProductCreation(creation));

        return newProductCode;
    }

    private CreateProductInput validateCreateProductInput(CreateProductInput input) {
        var productInput = ProductValidator.validate(input);
        var specifications = SpecificationValidator.validate(input.specifications());
        var skus = SkuValidator.validate(input.skus());
        SkuSpecificationValidator.validate(specifications, skus);

        return new CreateProductInput(
                productInput.title(),
                productInput.subtitle(),
                productInput.description(),
                productInput.categoryCode(),
                productInput.primaryImageFileId(),
                productInput.galleryImageFileIds(),
                specifications,
                skus
        );
    }

    private void saveProductCreation(ProductCreation creation) {
        var productCode = creation.product().productCode();
        productRepository.create(creation.product());
        specificationRepository.create(productCode, creation.specifications());
        skuRepository.create(productCode, creation.skus());
    }
}

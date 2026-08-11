package org.ayachinene.api.product;

import org.ayachinene.api.product.data.CreateProductRequest;
import org.ayachinene.api.product.data.PublishProductResponse;
import org.ayachinene.app.product.creation.CreateProductInput;
import org.ayachinene.app.product.domain.CategoryCode;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.publication.PublishProductInput;
import org.ayachinene.app.product.publication.PublishProductResult;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.utils.Streams;
import org.ayachinene.utils.Values;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductApiMapper {

    public CreateProductInput toInput(CreateProductRequest request) {
        return new CreateProductInput(
                request.title(),
                request.subtitle(),
                request.description(),
                categoryCode(request.categoryCode()),
                fileId(request.primaryImageFileId()),
                fileIds(request.galleryImageFileIds()),
                specifications(request.specifications()),
                skus(request.skus())
        );
    }

    private CategoryCode categoryCode(String value) {
        return Values.map(value, CategoryCode::new);
    }

    private UUID7 fileId(String value) {
        return Values.map(
                value,
                input -> UUID7.fromString(input, "fileResourceId")
        );
    }

    private List<UUID7> fileIds(List<String> values) {
        return Streams.of(values).map(this::fileId).toList();
    }

    private List<CreateProductInput.Specification> specifications(
            List<CreateProductRequest.SpecificationRequest> requests
    ) {
        return Streams.of(requests)
                .map(this::specification)
                .toList();
    }

    private CreateProductInput.Specification specification(
            CreateProductRequest.SpecificationRequest request
    ) {
        return Values.map(request, value -> new CreateProductInput.Specification(
                value.name(),
                value.values()
        ));
    }

    private List<CreateProductInput.Sku> skus(
            List<CreateProductRequest.SkuRequest> requests
    ) {
        return Streams.of(requests).map(this::sku).toList();
    }

    private CreateProductInput.Sku sku(CreateProductRequest.SkuRequest request) {
        return Values.map(request, value -> new CreateProductInput.Sku(
                value.merchantSkuCode(),
                value.price(),
                fileId(value.imageFileId()),
                selections(value.selections())
        ));
    }

    private List<CreateProductInput.Selection> selections(
            List<CreateProductRequest.SelectionRequest> requests
    ) {
        return Streams.of(requests).map(this::selection).toList();
    }

    private CreateProductInput.Selection selection(
            CreateProductRequest.SelectionRequest request
    ) {
        return Values.map(request, value -> new CreateProductInput.Selection(
                value.specification(),
                value.value()
        ));
    }

    public PublishProductInput toInput(
        String productCode,
        long expectedVersion
    ) {
        return new PublishProductInput(
            new ProductCode(productCode),
            expectedVersion
        );
    }

    public PublishProductResponse toResponse(PublishProductResult result) {
        return new PublishProductResponse(
            result.productCode().value(),
            result.status().name(),
            result.version()
        );
    }

}

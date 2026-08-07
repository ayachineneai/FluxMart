package org.ayachinene.api.product;

import org.ayachinene.api.product.data.CreateProductRequest;
import org.ayachinene.api.product.data.PublishProductRequest;
import org.ayachinene.api.product.data.PublishProductResponse;
import org.ayachinene.app.domain.product.CategoryCode;
import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.domain.product.creation.*;
import org.ayachinene.app.domain.product.publication.PublishProductInput;
import org.ayachinene.app.domain.product.publication.PublishProductResult;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.utils.Streams;
import org.ayachinene.utils.Validates;
import org.ayachinene.utils.Values;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductApiMapper {

    public CreateProductInput toInput(CreateProductRequest request) {
        return new CreateProductInput(
            product(request),
            specifications(request.specifications()),
            skus(request.skus())
        );
    }

    public PublishProductInput toInput(
        String productCode,
        PublishProductRequest request
    ) {
        return new PublishProductInput(
            new ProductCode(productCode),
            Validates.requireNonNull(request.expectedVersion(), "expectedVersion")
        );
    }

    public PublishProductResponse toResponse(PublishProductResult result) {
        return new PublishProductResponse(
            result.productCode().value(),
            result.status().name(),
            result.version()
        );
    }

    private ProductInput product(CreateProductRequest request) {
        return new ProductInput(
            request.title(),
            request.subtitle(),
            request.description(),
            categoryCode(request.categoryCode()),
            fileResourceId(request.primaryImageFileId()),
            fileResourceIds(request.galleryImageFileIds())
        );
    }

    private CategoryCode categoryCode(String value) {
        return Values.map(value, CategoryCode::new);
    }

    private UUID7 fileResourceId(String value) {
        return Values.map(
            value,
            input -> UUID7.fromString(input, "fileResourceId")
        );
    }

    private List<UUID7> fileResourceIds(List<String> values) {
        return Streams.of(values)
            .map(this::fileResourceId)
            .toList();
    }

    private List<SpecificationInput> specifications(
        List<CreateProductRequest.SpecificationRequest> requests
    ) {
        return Streams.of(requests)
            .map(this::specification)
            .toList();
    }

    private SpecificationInput specification(
        CreateProductRequest.SpecificationRequest request
    ) {
        return Values.map(
            request,
            value -> new SpecificationInput(value.name(), value.values())
        );
    }

    private List<SkuInput> skus(List<CreateProductRequest.SkuRequest> requests) {
        return Streams.of(requests)
            .map(this::sku)
            .toList();
    }

    private SkuInput sku(CreateProductRequest.SkuRequest request) {
        return Values.map(
            request,
            value -> new SkuInput(
                value.merchantSkuCode(),
                value.price(),
                fileResourceId(value.imageFileId()),
                selections(value.selections())
            )
        );
    }

    private List<SelectionInput> selections(
        List<CreateProductRequest.SelectionRequest> requests
    ) {
        return Streams.of(requests)
            .map(this::selection)
            .toList();
    }

    private SelectionInput selection(CreateProductRequest.SelectionRequest request) {
        return Values.map(
            request,
            value -> new SelectionInput(value.specification(), value.value())
        );
    }

}

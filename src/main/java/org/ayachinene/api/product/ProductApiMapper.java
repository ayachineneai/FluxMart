package org.ayachinene.api.product;

import org.ayachinene.api.product.data.CreateProductRequest;
import org.ayachinene.app.domain.product.CategoryCode;
import org.ayachinene.app.domain.product.creation.CreateProductInput;
import org.ayachinene.app.domain.product.creation.SelectionInput;
import org.ayachinene.app.domain.product.creation.SkuInput;
import org.ayachinene.app.domain.product.creation.SpecificationInput;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.utils.Lists;
import org.ayachinene.utils.Values;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ProductApiMapper {

    public CreateProductInput toInput(CreateProductRequest request) {
        Objects.requireNonNull(request, "request must not be null");
        return new CreateProductInput(
                request.title(),
                request.subtitle(),
                request.description(),
                categoryCode(request.categoryCode()),
                fileResourceId(request.primaryImageFileId()),
                fileResourceIds(request.galleryImageFileIds()),
                specifications(request.specifications()),
                skus(request.skus())
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
        return Lists.nullToEmpty(values).stream()
                .map(this::fileResourceId)
                .toList();
    }

    private List<SpecificationInput> specifications(
            List<CreateProductRequest.SpecificationRequest> requests
    ) {
        return Lists.nullToEmpty(requests).stream()
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
        return Lists.nullToEmpty(requests).stream()
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
        return Lists.nullToEmpty(requests).stream()
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

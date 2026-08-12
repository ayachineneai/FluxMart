package org.ayachinene.app.product.creation;

import org.ayachinene.api.product.data.CreateProductRequest;
import org.ayachinene.utils.Streams;

import java.util.List;

import static org.ayachinene.utils.Lists.uniqueNonNull;
import static org.ayachinene.utils.Validates.*;

public final class ProductValidator {

    private ProductValidator() {
    }

    public static CreateProductRequest validate(CreateProductRequest request) {
        notNull(request, "request");
        return new CreateProductRequest(
            text(request.title(), "title", 50),
            optionalText(request.subtitle(), "subtitle", 50),
            text(request.description(), "description", 5000),
            text(request.categoryCode(), "categoryCode", 64),
            notNull(request.primaryImageFileId(), "primaryImageFileId"),
            uniqueNonNull(request.galleryImageFileIds()),
            validateSpecifications(request.specifications()),
            request.skus()
        );
    }

    private static List<CreateProductRequest.SpecificationRequest> validateSpecifications(
        List<CreateProductRequest.SpecificationRequest> requests
    ) {
        var specifications = Streams.withIndex(requests)
            .map(x -> validateSpecification(x.value(), x.index()))
            .toList();
        notDuplicated(
            Streams.of(specifications)
                .map(CreateProductRequest.SpecificationRequest::name)
                .toList(),
            "specification names must not contain duplicates"
        );
        return specifications;
    }

    private static CreateProductRequest.SpecificationRequest validateSpecification(
        CreateProductRequest.SpecificationRequest request,
        int index
    ) {
        var field = "specifications[" + index + "]";
        notNull(request, field);
        return new CreateProductRequest.SpecificationRequest(
            text(request.name(), field + ".name", 50),
            validateSpecificationValues(request.values(), field)
        );
    }

    private static List<String> validateSpecificationValues(
        List<String> values,
        String specificationField
    ) {
        require(
            values != null && !values.isEmpty(),
            specificationField + ".values must not be empty"
        );
        var validated = Streams.withIndex(values)
            .map(x -> text(
                x.value(),
                specificationField + ".values[" + x.index() + "]",
                50
            ))
            .toList();
        notDuplicated(
            validated,
            specificationField + ".values must not contain duplicates"
        );
        return validated;
    }
}

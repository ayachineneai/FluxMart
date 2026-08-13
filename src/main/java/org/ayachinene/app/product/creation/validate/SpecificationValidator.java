package org.ayachinene.app.product.creation.validate;

import org.ayachinene.api.product.data.CreateProductRequest;

import java.util.LinkedHashMap;
import java.util.List;

import static org.ayachinene.utils.Validates.require;
import static org.ayachinene.utils.Validates.text;

public final class SpecificationValidator {

    private SpecificationValidator() {
    }

    public static void validate(
        LinkedHashMap<String, List<String>> specifications
    ) {
        specifications.forEach((name, values) -> {
            text(name, "specification name", 50);
            values(values);
        });
    }

    private static void values(List<String> values) {
        require(!values.isEmpty(), "specification values must not be empty");
        values.forEach(value -> text(value, "specification value", 50));
    }
}

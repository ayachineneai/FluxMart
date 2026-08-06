package org.ayachinene.app.domain.product.specification;

import org.ayachinene.app.domain.product.creation.SpecificationInput;
import org.ayachinene.utils.Lists;
import org.ayachinene.utils.Validates;

import java.util.List;

public final class SpecificationValidator {

    private static final int MAX_NAME_LENGTH = 50;

    private SpecificationValidator() {
    }

    public static List<SpecificationInput> validate(List<SpecificationInput> inputs) {
        var specifications = Lists.nullToEmpty(inputs).stream()
                .map(SpecificationValidator::validate)
                .toList();
        Validates.requireUnique(
                specifications,
                SpecificationInput::name,
                "specification names must not be duplicated"
        );
        return specifications;
    }

    private static SpecificationInput validate(SpecificationInput input) {
        Validates.requireNonNull(input, "specifications element");
        var name = Validates.requiredText(
                input.name(),
                "specification name",
                MAX_NAME_LENGTH
        );
        return new SpecificationInput(name, values(input.values(), name));
    }

    private static List<String> values(List<String> inputs, String specificationName) {
        var values = Lists.nullToEmpty(inputs).stream()
                .map(input -> Validates.requiredText(
                        input,
                        "specification value",
                        MAX_NAME_LENGTH
                ))
                .toList();
        Validates.require(
                !values.isEmpty(),
                "specification values must not be empty: " + specificationName
        );
        Validates.requireUnique(
                values,
                "specification values must not be duplicated: " + specificationName
        );
        return values;
    }

}

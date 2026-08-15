package org.ayachinene.app.order.creation;

public record SpecificationSelection(
    String specificationCode,
    String specificationName,
    String specificationValueCode,
    String specificationValue
) {
}

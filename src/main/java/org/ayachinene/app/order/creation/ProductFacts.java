package org.ayachinene.app.order.creation;

import java.util.List;

public record ProductFacts(
    ProductBaseInfo baseInfo,
    List<SpecificationSelection> specificationSelections
) {
}

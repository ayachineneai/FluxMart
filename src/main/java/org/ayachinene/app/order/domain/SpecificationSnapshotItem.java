package org.ayachinene.app.order.domain;

import org.ayachinene.shared.uuid7.UUID7;

public record SpecificationSnapshotItem(
        UUID7 specificationId,
        String specificationName,
        UUID7 specificationValueId,
        String specificationValueName
) {
}

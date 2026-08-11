package org.ayachinene.infra.product.persistence.specification;

import org.ayachinene.app.product.domain.specification.SpecificationCode;
import org.ayachinene.app.product.domain.specification.SpecificationValueCode;
import org.ayachinene.shared.uuid7.UUID7;

import java.util.Map;

public record SpecificationPersistenceIds(
        Map<SpecificationCode, UUID7> specificationIds,
        Map<SpecificationValueCode, UUID7> specificationValueIds
) {

    public SpecificationPersistenceIds {
        specificationIds = Map.copyOf(specificationIds);
        specificationValueIds = Map.copyOf(specificationValueIds);
    }

    public UUID7 specificationId(SpecificationCode code) {
        return requireId(specificationIds, code, "specification");
    }

    public UUID7 specificationValueId(SpecificationValueCode code) {
        return requireId(specificationValueIds, code, "specification value");
    }

    private static <K> UUID7 requireId(Map<K, UUID7> ids, K code, String type) {
        var id = ids.get(code);
        if (id == null) {
            throw new IllegalStateException("Missing persistence ID for " + type + ": " + code);
        }
        return id;
    }
}

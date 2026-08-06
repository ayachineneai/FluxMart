package org.ayachinene.infra.persistence.product.converter;

import org.ayachinene.app.domain.product.specification.Specification;
import org.ayachinene.app.domain.product.specification.SpecificationValue;
import org.ayachinene.infra.persistence.product.specification.SpecificationPO;
import org.ayachinene.infra.persistence.product.specification.SpecificationValuePO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface SpecificationPersistenceConverter {

    @Mapping(target = "id", source = "specificationId")
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "sortOrder", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SpecificationPO toSpecificationPo(Specification specification);

    @Mapping(target = "id", source = "specificationValueId")
    @Mapping(target = "specificationId", ignore = true)
    @Mapping(target = "sortOrder", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SpecificationValuePO toSpecificationValuePo(SpecificationValue value);
}

package org.ayachinene.infra.product.persistence.converter;

import org.ayachinene.app.product.domain.specification.Specification;
import org.ayachinene.app.product.domain.specification.SpecificationValue;
import org.ayachinene.infra.product.persistence.specification.SpecificationPO;
import org.ayachinene.infra.product.persistence.specification.SpecificationValuePO;
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

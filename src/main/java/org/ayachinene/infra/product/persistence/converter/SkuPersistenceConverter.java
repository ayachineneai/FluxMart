package org.ayachinene.infra.product.persistence.converter;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.product.domain.sku.Sku;
import org.ayachinene.app.product.domain.sku.SpecificationSelection;
import org.ayachinene.infra.product.persistence.sku.SkuPO;
import org.ayachinene.infra.product.persistence.sku.SkuSpecificationSelectionPO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface SkuPersistenceConverter {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "priceAmount", source = "price")
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    SkuPO toSkuPo(Sku sku);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "skuId", ignore = true)
    @Mapping(target = "specificationId", ignore = true)
    @Mapping(target = "specificationValueId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    SkuSpecificationSelectionPO toSelectionPo(SpecificationSelection selection);

    default Long toMinorAmount(Money money) {
        return money.amount().movePointRight(2).longValueExact();
    }
}

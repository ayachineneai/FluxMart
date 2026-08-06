package org.ayachinene.infra.persistence.product.converter;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.domain.product.sku.Sku;
import org.ayachinene.app.domain.product.sku.SpecificationSelection;
import org.ayachinene.infra.persistence.product.sku.SkuPO;
import org.ayachinene.infra.persistence.product.sku.SkuSpecificationSelectionPO;
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
    @Mapping(target = "createdAt", ignore = true)
    SkuSpecificationSelectionPO toSelectionPo(SpecificationSelection selection);

    default Long toMinorAmount(Money money) {
        return money.amount().movePointRight(2).longValueExact();
    }
}

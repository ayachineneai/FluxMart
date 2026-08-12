package org.ayachinene.app.product.creation;

import org.ayachinene.infra.product.persistence.SkuPO;
import org.ayachinene.infra.product.persistence.SkuSpecificationSelectionPO;
import org.ayachinene.infra.product.persistence.StockPO;

import java.util.List;

public record SkuPos(
    SkuPO sku,
    List<SkuSpecificationSelectionPO> selections,
    StockPO stock
) {
}

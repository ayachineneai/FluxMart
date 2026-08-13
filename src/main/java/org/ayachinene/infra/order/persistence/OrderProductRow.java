package org.ayachinene.infra.order.persistence;

import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.domain.sku.SkuStatus;
import org.ayachinene.shared.uuid7.UUID7;

public final class OrderProductRow {

    private UUID7 skuId;
    private String skuCode;
    private SkuStatus skuStatus;
    private Long priceAmount;
    private UUID7 stockId;
    private String productCode;
    private ProductStatus productStatus;
    private String productTitle;
    private UUID7 snapshotImageFileId;

    public UUID7 getSkuId() { return skuId; }
    public void setSkuId(UUID7 value) { skuId = value; }

    public String getSkuCode() { return skuCode; }
    public void setSkuCode(String value) { skuCode = value; }

    public SkuStatus getSkuStatus() { return skuStatus; }
    public void setSkuStatus(SkuStatus value) { skuStatus = value; }

    public Long getPriceAmount() { return priceAmount; }
    public void setPriceAmount(Long value) { priceAmount = value; }

    public UUID7 getStockId() { return stockId; }
    public void setStockId(UUID7 value) { stockId = value; }

    public String getProductCode() { return productCode; }
    public void setProductCode(String value) { productCode = value; }

    public ProductStatus getProductStatus() { return productStatus; }
    public void setProductStatus(ProductStatus value) { productStatus = value; }

    public String getProductTitle() { return productTitle; }
    public void setProductTitle(String value) { productTitle = value; }

    public UUID7 getSnapshotImageFileId() { return snapshotImageFileId; }
    public void setSnapshotImageFileId(UUID7 value) { snapshotImageFileId = value; }
}

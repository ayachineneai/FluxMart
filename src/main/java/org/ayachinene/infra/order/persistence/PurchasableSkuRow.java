package org.ayachinene.infra.order.persistence;

import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.domain.sku.SkuStatus;
import org.ayachinene.shared.uuid7.UUID7;

public class PurchasableSkuRow {

    private UUID7 skuId;
    private ProductCode productCode;
    private SkuCode skuCode;
    private ProductStatus productStatus;
    private SkuStatus skuStatus;
    private String productTitle;
    private UUID7 specificationId;
    private String specificationName;
    private UUID7 specificationValueId;
    private String specificationValueName;
    private UUID7 imageFileId;
    private Long priceAmount;

    public UUID7 getSkuId() {
        return skuId;
    }

    public void setSkuId(UUID7 skuId) {
        this.skuId = skuId;
    }

    public ProductCode getProductCode() {
        return productCode;
    }

    public void setProductCode(ProductCode productCode) {
        this.productCode = productCode;
    }

    public SkuCode getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(SkuCode skuCode) {
        this.skuCode = skuCode;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(ProductStatus productStatus) {
        this.productStatus = productStatus;
    }

    public SkuStatus getSkuStatus() {
        return skuStatus;
    }

    public void setSkuStatus(SkuStatus skuStatus) {
        this.skuStatus = skuStatus;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public UUID7 getSpecificationId() {
        return specificationId;
    }

    public void setSpecificationId(UUID7 specificationId) {
        this.specificationId = specificationId;
    }

    public String getSpecificationName() {
        return specificationName;
    }

    public void setSpecificationName(String specificationName) {
        this.specificationName = specificationName;
    }

    public UUID7 getSpecificationValueId() {
        return specificationValueId;
    }

    public void setSpecificationValueId(UUID7 specificationValueId) {
        this.specificationValueId = specificationValueId;
    }

    public String getSpecificationValueName() {
        return specificationValueName;
    }

    public void setSpecificationValueName(String specificationValueName) {
        this.specificationValueName = specificationValueName;
    }

    public UUID7 getImageFileId() {
        return imageFileId;
    }

    public void setImageFileId(UUID7 imageFileId) {
        this.imageFileId = imageFileId;
    }

    public Long getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(Long priceAmount) {
        this.priceAmount = priceAmount;
    }
}

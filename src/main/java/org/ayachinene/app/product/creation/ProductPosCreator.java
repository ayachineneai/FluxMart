package org.ayachinene.app.product.creation;

import org.ayachinene.api.product.data.CreateProductRequest;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.domain.specification.SpecificationCode;
import org.ayachinene.app.product.domain.specification.SpecificationStatus;
import org.ayachinene.app.product.domain.specification.SpecificationValueCode;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.domain.sku.SkuStatus;
import org.ayachinene.infra.product.persistence.ProductGalleryImagePO;
import org.ayachinene.infra.product.persistence.ProductPO;
import org.ayachinene.infra.product.persistence.ProductSpecificationPO;
import org.ayachinene.infra.product.persistence.ProductSpecificationValuePO;
import org.ayachinene.infra.product.persistence.SkuPO;
import org.ayachinene.infra.product.persistence.SkuSpecificationSelectionPO;
import org.ayachinene.infra.product.persistence.StockPO;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.shared.uuid7.UUID7s;
import org.ayachinene.utils.Lists;
import org.ayachinene.utils.Streams;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ProductPosCreator {

    private static final BigDecimal MINOR_UNIT = BigDecimal.valueOf(100);

    private ProductPosCreator() {
    }

    public static ProductCreationPos mkPos(CreateProductRequest request) {
        var createdAt = LocalDateTime.now();
        var product = mkProduct(
            UUID7s.generate(),
            ProductCode.generate(),
            createdAt,
            request
        );
        var productGalleryImages = mkProductGalleryImages(
            product.getId(),
            createdAt,
            request.galleryImageFileIds()
        );
        var productSpecificationPos = mkProductSpecifications(
            product.getId(),
            createdAt,
            request.specifications()
        );
        var skuPos = mkSkus(
            product.getId(),
            createdAt,
            productSpecificationPos,
            request.skus()
        );
        return new ProductCreationPos(
            product,
            productGalleryImages,
            Streams.of(productSpecificationPos)
                .map(ProductSpecificationPos::specification)
                .toList(),
            Streams.of(productSpecificationPos)
                .flatMap(x -> x.values().stream())
                .toList(),
            Streams.of(skuPos)
                .map(SkuPos::sku)
                .toList(),
            Streams.of(skuPos)
                .flatMap(x -> x.selections().stream())
                .toList(),
            Streams.of(skuPos)
                .map(SkuPos::stock)
                .toList()
        );
    }

    private static ProductPO mkProduct(
        UUID7 id,
        ProductCode productCode,
        LocalDateTime createdAt,
        CreateProductRequest request
    ) {
        return new ProductPO()
            .setId(id)
            .setProductCode(productCode)
            .setStatus(ProductStatus.DRAFT)
            .setTitle(request.title())
            .setSubtitle(request.subtitle())
            .setDescription(request.description())
            .setCategoryCode(request.categoryCode())
            .setPrimaryImageFileId(request.primaryImageFileId())
            .setVersion(0L)
            .setCreatedAt(createdAt)
            .setUpdatedAt(createdAt);
    }

    private static List<ProductGalleryImagePO> mkProductGalleryImages(
        UUID7 productId,
        LocalDateTime createdAt,
        List<UUID7> imageFileIds
    ) {
        return Streams.withIndex(imageFileIds)
            .map(x -> {
                UUID7 id = UUID7s.generate();
                return new ProductGalleryImagePO()
                    .setId(id)
                    .setProductId(productId)
                    .setFileId(x.value())
                    .setSortOrder(x.index())
                    .setCreatedAt(createdAt);
            })
            .toList();
    }

    private static List<ProductSpecificationPos> mkProductSpecifications(
        UUID7 productId,
        LocalDateTime createdAt,
        LinkedHashMap<String, List<String>> requests
    ) {
        return Streams.withIndex(new ArrayList<>(requests.entrySet()))
            .map(x -> {
                var specificationId = UUID7s.generate();
                var specification = new ProductSpecificationPO()
                    .setId(specificationId)
                    .setProductId(productId)
                    .setSpecificationCode(SpecificationCode.generate())
                    .setName(x.value().getKey())
                    .setStatus(SpecificationStatus.ENABLED)
                    .setSortOrder(x.index())
                    .setCreatedAt(createdAt)
                    .setUpdatedAt(createdAt);
                var values = mkProductSpecificationValues(
                    specificationId,
                    createdAt,
                    x.value().getValue()
                );
                return new ProductSpecificationPos(specification, values);
            })
            .toList();
    }

    private static List<ProductSpecificationValuePO> mkProductSpecificationValues(
        UUID7 specificationId,
        LocalDateTime createdAt,
        List<String> values
    ) {
        return Streams.withIndex(values)
            .map(x -> new ProductSpecificationValuePO()
                .setId(UUID7s.generate())
                .setSpecificationId(specificationId)
                .setSpecificationValueCode(SpecificationValueCode.generate())
                .setDisplayName(x.value())
                .setStatus(SpecificationStatus.ENABLED)
                .setSortOrder(x.index())
                .setCreatedAt(createdAt)
                .setUpdatedAt(createdAt)
            )
            .toList();
    }

    private static List<SkuPos> mkSkus(
        UUID7 productId,
        LocalDateTime createdAt,
        List<ProductSpecificationPos> specificationPos,
        List<CreateProductRequest.SkuRequest> requests
    ) {
        return Streams.of(requests)
            .map(request -> mkSku(
                productId,
                createdAt,
                specificationPos,
                request
            ))
            .toList();
    }

    private static SkuPos mkSku(
        UUID7 productId,
        LocalDateTime createdAt,
        List<ProductSpecificationPos> specificationPos,
        CreateProductRequest.SkuRequest request
    ) {
        var skuId = UUID7s.generate();
        var sku = new SkuPO()
            .setId(skuId)
            .setProductId(productId)
            .setSkuCode(SkuCode.generate())
            .setMerchantSkuCode(request.merchantSkuCode())
            .setStatus(SkuStatus.ENABLED)
            .setPriceAmount(request.price().multiply(MINOR_UNIT).longValueExact())
            .setImageFileId(request.imageFileId())
            .setVersion(0L)
            .setCreatedAt(createdAt)
            .setUpdatedAt(createdAt);
        var selections = mkSkuSpecificationSelections(
            skuId,
            createdAt,
            specificationPos,
            request.selections()
        );
        var stock = new StockPO()
            .setId(UUID7s.generate())
            .setSkuId(skuId)
            .setAvailableQuantity(0L)
            .setReservedQuantity(0L)
            .setVersion(0L)
            .setCreatedAt(createdAt)
            .setUpdatedAt(createdAt);
        return new SkuPos(sku, selections, stock);
    }

    private static List<SkuSpecificationSelectionPO> mkSkuSpecificationSelections(
        UUID7 skuId,
        LocalDateTime createdAt,
        List<ProductSpecificationPos> specificationPos,
        Map<String, String> selections
    ) {
        return Streams.of(selections.entrySet())
            .map(selection -> {
                var idPair = specificationSelectionIdPairs(
                    specificationPos,
                    selection.getKey(),
                    selection.getValue()
                );
                return new SkuSpecificationSelectionPO()
                    .setId(UUID7s.generate())
                    .setSkuId(skuId)
                    .setSpecificationId(idPair.specificationId())
                    .setSpecificationValueId(idPair.specificationValueId())
                    .setCreatedAt(createdAt);
            })
            .toList();
    }

    private static SpecificationSelectionIdPair specificationSelectionIdPairs(
        List<ProductSpecificationPos> specificationPos,
        String specificationName,
        String specificationValue
    ) {
        var specification = Lists.find(specificationPos,
            x -> x.specification().getName().equals(specificationName)).get();
        var value = Lists.find(specification.values(),
            x -> x.getDisplayName().equals(specificationValue)).get();
        return new SpecificationSelectionIdPair(
            specification.specification().getId(),
            value.getId()
        );
    }

    private record SpecificationSelectionIdPair(
        UUID7 specificationId,
        UUID7 specificationValueId
    ) {
    }
}

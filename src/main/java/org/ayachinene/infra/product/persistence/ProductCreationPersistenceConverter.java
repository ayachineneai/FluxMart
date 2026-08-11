package org.ayachinene.infra.product.persistence;

import org.ayachinene.app.product.creation.ProductCreation;
import org.ayachinene.app.product.domain.sku.Sku;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.domain.specification.Specification;
import org.ayachinene.app.product.domain.specification.SpecificationCode;
import org.ayachinene.app.product.domain.specification.SpecificationValueCode;
import org.ayachinene.infra.product.persistence.converter.ProductPersistenceConverter;
import org.ayachinene.infra.product.persistence.converter.SkuPersistenceConverter;
import org.ayachinene.infra.product.persistence.converter.SpecificationPersistenceConverter;
import org.ayachinene.infra.product.persistence.sku.SkuPO;
import org.ayachinene.infra.product.persistence.sku.SkuSpecificationSelectionPO;
import org.ayachinene.infra.product.persistence.specification.SpecificationPO;
import org.ayachinene.infra.product.persistence.specification.SpecificationValuePO;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.shared.uuid7.UUID7s;
import org.ayachinene.utils.Streams;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductCreationPersistenceConverter {

    private static final long INITIAL_VERSION = 0L;

    private final ProductPersistenceConverter productConverter;
    private final SpecificationPersistenceConverter specificationConverter;
    private final SkuPersistenceConverter skuConverter;

    public ProductCreationPersistenceConverter(
            ProductPersistenceConverter productConverter,
            SpecificationPersistenceConverter specificationConverter,
            SkuPersistenceConverter skuConverter
    ) {
        this.productConverter = productConverter;
        this.specificationConverter = specificationConverter;
        this.skuConverter = skuConverter;
    }

    public ProductCreationPOs toPos(ProductCreation creation) {
        var productId = UUID7s.generate();
        var createdAt = LocalDateTime.now();
        var specificationIds = specificationIds(creation.specifications());
        var specificationValueIds = specificationValueIds(
                creation.specifications()
        );
        var skuIds = skuIds(creation.skus());

        return new ProductCreationPOs(
                product(creation, productId, createdAt),
                galleryImages(creation, productId, createdAt),
                specifications(
                        creation.specifications(),
                        productId,
                        specificationIds,
                        createdAt
                ),
                specificationValues(
                        creation.specifications(),
                        specificationIds,
                        specificationValueIds,
                        createdAt
                ),
                skus(creation.skus(), productId, skuIds, createdAt),
                selections(
                        creation.skus(),
                        skuIds,
                        specificationIds,
                        specificationValueIds,
                        createdAt
                )
        );
    }

    private Map<SpecificationCode, UUID7> specificationIds(
            List<Specification> specifications
    ) {
        var ids = new LinkedHashMap<SpecificationCode, UUID7>();
        specifications.forEach(specification -> ids.put(
                specification.specificationCode(),
                UUID7s.generate()
        ));
        return ids;
    }

    private Map<SpecificationValueCode, UUID7> specificationValueIds(
            List<Specification> specifications
    ) {
        var ids = new LinkedHashMap<SpecificationValueCode, UUID7>();
        specifications.forEach(specification -> specification.values()
                .forEach(value -> ids.put(
                        value.specificationValueCode(),
                        UUID7s.generate()
                ))
        );
        return ids;
    }

    private Map<SkuCode, UUID7> skuIds(List<Sku> skus) {
        var ids = new LinkedHashMap<SkuCode, UUID7>();
        skus.forEach(sku -> ids.put(sku.skuCode(), UUID7s.generate()));
        return ids;
    }

    private ProductPO product(
            ProductCreation creation,
            UUID7 productId,
            LocalDateTime createdAt
    ) {
        return productConverter.toProductPo(creation.product())
                .setId(productId)
                .setVersion(INITIAL_VERSION)
                .setCreatedAt(createdAt)
                .setUpdatedAt(createdAt);
    }

    private List<ProductGalleryImagePO> galleryImages(
            ProductCreation creation,
            UUID7 productId,
            LocalDateTime createdAt
    ) {
        return Streams.withIndex(creation.product().galleryImageFileIds())
                .map(indexed -> productConverter
                        .toGalleryImagePo(indexed.value())
                        .setId(UUID7s.generate())
                        .setProductId(productId)
                        .setSortOrder(indexed.index())
                        .setCreatedAt(createdAt)
                )
                .toList();
    }

    private List<SpecificationPO> specifications(
            List<Specification> specifications,
            UUID7 productId,
            Map<SpecificationCode, UUID7> specificationIds,
            LocalDateTime createdAt
    ) {
        return Streams.withIndex(specifications)
                .map(indexed -> specificationConverter
                        .toSpecificationPo(indexed.value())
                        .setId(idByCode(
                                specificationIds,
                                indexed.value().specificationCode()
                        ))
                        .setProductId(productId)
                        .setSortOrder(indexed.index())
                        .setCreatedAt(createdAt)
                        .setUpdatedAt(createdAt)
                )
                .toList();
    }

    private List<SpecificationValuePO> specificationValues(
            List<Specification> specifications,
            Map<SpecificationCode, UUID7> specificationIds,
            Map<SpecificationValueCode, UUID7> specificationValueIds,
            LocalDateTime createdAt
    ) {
        return specifications.stream()
                .flatMap(specification -> Streams.withIndex(specification.values())
                        .map(indexed -> specificationConverter
                                .toSpecificationValuePo(indexed.value())
                                .setId(idByCode(
                                        specificationValueIds,
                                        indexed.value().specificationValueCode()
                                ))
                                .setSpecificationId(idByCode(
                                        specificationIds,
                                        specification.specificationCode()
                                ))
                                .setSortOrder(indexed.index())
                                .setCreatedAt(createdAt)
                                .setUpdatedAt(createdAt)
                        )
                )
                .toList();
    }

    private List<SkuPO> skus(
            List<Sku> skus,
            UUID7 productId,
            Map<SkuCode, UUID7> skuIds,
            LocalDateTime createdAt
    ) {
        return skus.stream()
                .map(sku -> skuConverter.toSkuPo(sku)
                        .setId(idByCode(skuIds, sku.skuCode()))
                        .setProductId(productId)
                        .setVersion(INITIAL_VERSION)
                        .setCreatedAt(createdAt)
                        .setUpdatedAt(createdAt)
                )
                .toList();
    }

    private List<SkuSpecificationSelectionPO> selections(
            List<Sku> skus,
            Map<SkuCode, UUID7> skuIds,
            Map<SpecificationCode, UUID7> specificationIds,
            Map<SpecificationValueCode, UUID7> specificationValueIds,
            LocalDateTime createdAt
    ) {
        return skus.stream()
                .flatMap(sku -> sku.specificationSelections().stream()
                        .map(selection -> skuConverter.toSelectionPo(selection)
                                .setId(UUID7s.generate())
                                .setSkuId(idByCode(skuIds, sku.skuCode()))
                                .setSpecificationId(idByCode(
                                        specificationIds,
                                        selection.specificationCode()
                                ))
                                .setSpecificationValueId(idByCode(
                                        specificationValueIds,
                                        selection.specificationValueCode()
                                ))
                                .setCreatedAt(createdAt)
                        )
                )
                .toList();
    }

    private <K> UUID7 idByCode(Map<K, UUID7> ids, K code) {
        var id = ids.get(code);
        if (id == null) {
            throw new IllegalStateException("Missing persistence ID for code: " + code);
        }
        return id;
    }
}

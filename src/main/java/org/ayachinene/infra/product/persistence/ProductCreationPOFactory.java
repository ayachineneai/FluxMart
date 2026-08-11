package org.ayachinene.infra.product.persistence;

import org.ayachinene.app.domain.money.Money;
import org.ayachinene.app.product.creation.CreateProductInput;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.app.product.domain.sku.SkuCode;
import org.ayachinene.app.product.domain.sku.SkuStatus;
import org.ayachinene.app.product.domain.specification.SpecificationCode;
import org.ayachinene.app.product.domain.specification.SpecificationStatus;
import org.ayachinene.app.product.domain.specification.SpecificationValueCode;
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
public class ProductCreationPOFactory {

    private static final long INITIAL_VERSION = 0L;

    public ProductCreationPOs toPos(CreateProductInput input) {
        var productId = UUID7s.generate();
        var createdAt = LocalDateTime.now();
        var specificationsByName = specificationIdentities(
            input.specifications()
        );
        var preparedSkus = prepareSkus(input.skus());

        var product = product(input, productId, createdAt);
        var galleryImages = galleryImages(input, productId, createdAt);
        var specifications = specifications(
            input.specifications(),
            specificationsByName,
            productId,
            createdAt
        );
        var specificationValues = specificationValues(
            input.specifications(),
            specificationsByName,
            createdAt
        );
        var skus = skus(preparedSkus, productId, createdAt);
        var selections = selections(
            preparedSkus,
            specificationsByName,
            createdAt
        );

        return new ProductCreationPOs(
            product,
            galleryImages,
            specifications,
            specificationValues,
            skus,
            selections
        );
    }

    private Map<String, SpecificationIdentity> specificationIdentities(
        List<CreateProductInput.Specification> specifications
    ) {
        var identities = new LinkedHashMap<String, SpecificationIdentity>();
        specifications.forEach(specification -> {
            var values = new LinkedHashMap<String, SpecificationValueIdentity>();
            specification.values().forEach(value -> values.put(
                value,
                new SpecificationValueIdentity(
                    UUID7s.generate(),
                    SpecificationValueCode.generate()
                )
            ));
            identities.put(
                specification.name(),
                new SpecificationIdentity(
                    UUID7s.generate(),
                    SpecificationCode.generate(),
                    values
                )
            );
        });
        return identities;
    }

    private List<PreparedSku> prepareSkus(
        List<CreateProductInput.Sku> skus
    ) {
        return skus.stream()
            .map(sku -> new PreparedSku(
                sku,
                UUID7s.generate(),
                SkuCode.generate()
            ))
            .toList();
    }

    private ProductPO product(
        CreateProductInput input,
        UUID7 productId,
        LocalDateTime createdAt
    ) {
        return new ProductPO()
            .setId(productId)
            .setProductCode(ProductCode.generate())
            .setStatus(ProductStatus.DRAFT)
            .setTitle(input.title())
            .setSubtitle(input.subtitle())
            .setDescription(input.description())
            .setCategoryCode(input.categoryCode())
            .setPrimaryImageFileId(input.primaryImageFileId())
            .setVersion(INITIAL_VERSION)
            .setCreatedAt(createdAt)
            .setUpdatedAt(createdAt);
    }

    private List<ProductGalleryImagePO> galleryImages(
        CreateProductInput input,
        UUID7 productId,
        LocalDateTime createdAt
    ) {
        return Streams.withIndex(input.galleryImageFileIds())
            .map(indexed -> new ProductGalleryImagePO()
                .setId(UUID7s.generate())
                .setProductId(productId)
                .setFileId(indexed.value())
                .setSortOrder(indexed.index())
                .setCreatedAt(createdAt)
            )
            .toList();
    }

    private List<SpecificationPO> specifications(
        List<CreateProductInput.Specification> specifications,
        Map<String, SpecificationIdentity> specificationsByName,
        UUID7 productId,
        LocalDateTime createdAt
    ) {
        return Streams.withIndex(specifications)
            .map(indexed -> {
                var identity = specificationIdentity(
                    specificationsByName,
                    indexed.value().name()
                );
                return new SpecificationPO()
                    .setId(identity.id())
                    .setProductId(productId)
                    .setSpecificationCode(identity.code())
                    .setName(indexed.value().name())
                    .setStatus(SpecificationStatus.ENABLED)
                    .setSortOrder(indexed.index())
                    .setCreatedAt(createdAt)
                    .setUpdatedAt(createdAt);
            })
            .toList();
    }

    private List<SpecificationValuePO> specificationValues(
        List<CreateProductInput.Specification> specifications,
        Map<String, SpecificationIdentity> specificationsByName,
        LocalDateTime createdAt
    ) {
        return specifications.stream()
            .flatMap(specification -> {
                var specificationIdentity = specificationIdentity(
                    specificationsByName,
                    specification.name()
                );
                return Streams.withIndex(specification.values())
                    .map(indexed -> {
                        var valueIdentity = valueIdentity(
                            specificationIdentity,
                            indexed.value()
                        );
                        return new SpecificationValuePO()
                            .setId(valueIdentity.id())
                            .setSpecificationId(
                                specificationIdentity.id()
                            )
                            .setSpecificationValueCode(
                                valueIdentity.code()
                            )
                            .setDisplayName(indexed.value())
                            .setStatus(SpecificationStatus.ENABLED)
                            .setSortOrder(indexed.index())
                            .setCreatedAt(createdAt)
                            .setUpdatedAt(createdAt);
                    });
            })
            .toList();
    }

    private List<SkuPO> skus(
        List<PreparedSku> preparedSkus,
        UUID7 productId,
        LocalDateTime createdAt
    ) {
        return preparedSkus.stream()
            .map(prepared -> new SkuPO()
                .setId(prepared.id())
                .setProductId(productId)
                .setSkuCode(prepared.code())
                .setMerchantSkuCode(prepared.input().merchantSkuCode())
                .setStatus(SkuStatus.ENABLED)
                .setPriceAmount(new Money(prepared.input().price())
                    .amount()
                    .movePointRight(2)
                    .longValueExact())
                .setImageFileId(prepared.input().imageFileId())
                .setVersion(INITIAL_VERSION)
                .setCreatedAt(createdAt)
                .setUpdatedAt(createdAt))
            .toList();
    }

    private List<SkuSpecificationSelectionPO> selections(
        List<PreparedSku> preparedSkus,
        Map<String, SpecificationIdentity> specificationsByName,
        LocalDateTime createdAt
    ) {
        return preparedSkus.stream()
            .flatMap(prepared -> prepared.input().selections().stream()
                .map(selection -> {
                    var specification = specificationIdentity(
                        specificationsByName,
                        selection.specification()
                    );
                    var value = valueIdentity(
                        specification,
                        selection.value()
                    );
                    return new SkuSpecificationSelectionPO()
                        .setId(UUID7s.generate())
                        .setSkuId(prepared.id())
                        .setSpecificationId(specification.id())
                        .setSpecificationValueId(value.id())
                        .setCreatedAt(createdAt);
                }))
            .toList();
    }

    private SpecificationIdentity specificationIdentity(
        Map<String, SpecificationIdentity> identities,
        String name
    ) {
        var identity = identities.get(name);
        if (identity == null) {
            throw new IllegalStateException(
                "Missing persistence identity for specification: " + name
            );
        }
        return identity;
    }

    private SpecificationValueIdentity valueIdentity(
        SpecificationIdentity specification,
        String value
    ) {
        var identity = specification.values().get(value);
        if (identity == null) {
            throw new IllegalStateException(
                "Missing persistence identity for specification value: " + value
            );
        }
        return identity;
    }

    private record SpecificationIdentity(
        UUID7 id,
        SpecificationCode code,
        Map<String, SpecificationValueIdentity> values
    ) {
    }

    private record SpecificationValueIdentity(
        UUID7 id,
        SpecificationValueCode code
    ) {
    }

    private record PreparedSku(
        CreateProductInput.Sku input,
        UUID7 id,
        SkuCode code
    ) {
    }
}

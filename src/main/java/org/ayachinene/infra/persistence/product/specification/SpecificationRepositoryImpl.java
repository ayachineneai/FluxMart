package org.ayachinene.infra.persistence.product.specification;

import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.domain.product.ProductNotFoundException;
import org.ayachinene.app.domain.product.specification.Specification;
import org.ayachinene.app.domain.product.specification.SpecificationRepository;
import org.ayachinene.app.uuid7.UUID7;
import org.ayachinene.infra.persistence.product.ProductMapper;
import org.ayachinene.infra.persistence.product.converter.SpecificationPersistenceConverter;
import org.ayachinene.utils.Streams;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class SpecificationRepositoryImpl implements SpecificationRepository {

    private final ProductMapper productMapper;
    private final SpecificationMapper specificationMapper;
    private final SpecificationValueMapper specificationValueMapper;
    private final SpecificationPersistenceConverter persistenceConverter;

    public SpecificationRepositoryImpl(
            ProductMapper productMapper,
            SpecificationMapper specificationMapper,
            SpecificationValueMapper specificationValueMapper,
            SpecificationPersistenceConverter persistenceConverter
    ) {
        this.productMapper = productMapper;
        this.specificationMapper = specificationMapper;
        this.specificationValueMapper = specificationValueMapper;
        this.persistenceConverter = persistenceConverter;
    }

    @Override
    public void create(
            ProductCode productCode,
            List<Specification> specifications
    ) {
        if (specifications.isEmpty()) {
            return;
        }
        var productId = productMapper.selectIdByProductCode(productCode);
        if (productId == null) {
            throw new ProductNotFoundException(productCode);
        }
        var createdAt = LocalDateTime.now();
        Streams.withIndex(specifications).forEach(indexed ->
                insertSpecification(indexed.value(), indexed.index(), productId, createdAt)
        );
    }

    private void insertSpecification(
            Specification specification,
            int sortOrder,
            UUID7 productId,
            LocalDateTime createdAt
    ) {
        var specificationPo = persistenceConverter.toSpecificationPo(specification)
                .setProductId(productId)
                .setSortOrder(sortOrder)
                .setCreatedAt(createdAt)
                .setUpdatedAt(createdAt);
        specificationMapper.insert(specificationPo);

        Streams.withIndex(specification.values()).forEach(indexed -> {
            var valuePo = persistenceConverter.toSpecificationValuePo(indexed.value())
                    .setSpecificationId(specification.specificationId())
                    .setSortOrder(indexed.index())
                    .setCreatedAt(createdAt)
                    .setUpdatedAt(createdAt);
            specificationValueMapper.insert(valuePo);
        });
    }
}

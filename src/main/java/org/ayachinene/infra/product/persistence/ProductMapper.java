package org.ayachinene.infra.product.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.ProductStatus;
import org.ayachinene.shared.uuid7.UUID7;
import org.ayachinene.utils.Values;

import java.time.LocalDateTime;

@Mapper
public interface ProductMapper extends BaseMapper<ProductPO> {

    default UUID7 selectIdByProductCode(ProductCode productCode) {
        return Values.map(selectOne(Wrappers.<ProductPO>lambdaQuery()
            .select(ProductPO::getId)
            .eq(ProductPO::getProductCode, productCode)
        ), ProductPO::getId);
    }

    default ProductPO queryPublicationStateByProductCode(
        ProductCode productCode
    ) {
        return selectOne(
            Wrappers.<ProductPO>lambdaQuery()
                .select(
                    ProductPO::getProductCode,
                    ProductPO::getStatus,
                    ProductPO::getVersion
                )
                .eq(ProductPO::getProductCode, productCode)
        );
    }

    default int updateByProductCodeAndVersion(ProductPO product, long expectedVersion) {
        return update(
            Wrappers.<ProductPO>lambdaUpdate()
                .set(ProductPO::getStatus, product.getStatus())
                .set(ProductPO::getTitle, product.getTitle())
                .set(ProductPO::getSubtitle, product.getSubtitle())
                .set(ProductPO::getDescription, product.getDescription())
                .set(ProductPO::getCategoryCode, product.getCategoryCode())
                .set(ProductPO::getPrimaryImageFileId, product.getPrimaryImageFileId())
                .set(ProductPO::getUpdatedAt, product.getUpdatedAt())
                .setIncrBy(ProductPO::getVersion, 1)
                .eq(ProductPO::getProductCode, product.getProductCode())
                .eq(ProductPO::getVersion, expectedVersion)
        );
    }

    default int updateStatusByProductCodeAndVersion(
        ProductCode productCode,
        ProductStatus status,
        long expectedVersion,
        LocalDateTime updatedAt
    ) {
        return update(
            Wrappers.<ProductPO>lambdaUpdate()
                .set(ProductPO::getStatus, status)
                .set(ProductPO::getUpdatedAt, updatedAt)
                .setIncrBy(ProductPO::getVersion, 1)
                .eq(ProductPO::getProductCode, productCode)
                .eq(ProductPO::getVersion, expectedVersion)
        );
    }
}

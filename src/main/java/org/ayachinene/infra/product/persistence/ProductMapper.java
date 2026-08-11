package org.ayachinene.infra.product.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.ayachinene.app.product.domain.ProductCode;
import org.ayachinene.app.product.domain.ProductStatus;

import java.time.LocalDateTime;

@Mapper
public interface ProductMapper extends BaseMapper<ProductPO> {

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

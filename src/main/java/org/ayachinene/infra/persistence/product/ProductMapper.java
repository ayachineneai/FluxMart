package org.ayachinene.infra.persistence.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.ayachinene.app.domain.product.ProductCode;
import org.ayachinene.app.uuid7.UUID7;
import org.ayachinene.utils.Values;

@Mapper
public interface ProductMapper extends BaseMapper<ProductPO> {

    default UUID7 selectIdByProductCode(ProductCode productCode) {
        return Values.map(selectOne(Wrappers.<ProductPO>lambdaQuery()
            .select(ProductPO::getId)
            .eq(ProductPO::getProductCode, productCode)
        ), ProductPO::getId);
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
}

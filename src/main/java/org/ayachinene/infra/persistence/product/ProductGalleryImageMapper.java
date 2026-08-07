package org.ayachinene.infra.persistence.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.ayachinene.shared.uuid7.UUID7;

import java.util.List;

@Mapper
public interface ProductGalleryImageMapper extends BaseMapper<ProductGalleryImagePO> {

    int insertBatch(List<ProductGalleryImagePO> images);

    default int deleteByProductId(UUID7 productId) {
        return delete(Wrappers.<ProductGalleryImagePO>lambdaQuery()
                .eq(ProductGalleryImagePO::getProductId, productId)
        );
    }
}

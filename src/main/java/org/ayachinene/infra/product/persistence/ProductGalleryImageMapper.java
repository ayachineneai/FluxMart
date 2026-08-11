package org.ayachinene.infra.product.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductGalleryImageMapper extends BaseMapper<ProductGalleryImagePO> {

    int insertBatch(List<ProductGalleryImagePO> images);
}

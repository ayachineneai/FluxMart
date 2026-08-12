package org.ayachinene.infra.product.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductSpecificationValueMapper
    extends BaseMapper<ProductSpecificationValuePO> {
}

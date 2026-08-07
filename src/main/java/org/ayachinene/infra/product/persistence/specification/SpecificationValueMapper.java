package org.ayachinene.infra.product.persistence.specification;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SpecificationValueMapper extends BaseMapper<SpecificationValuePO> {

    int insertBatch(List<SpecificationValuePO> values);
}

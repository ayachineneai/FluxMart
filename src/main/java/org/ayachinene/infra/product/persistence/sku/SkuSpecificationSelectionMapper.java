package org.ayachinene.infra.product.persistence.sku;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SkuSpecificationSelectionMapper
        extends BaseMapper<SkuSpecificationSelectionPO> {

    int insertBatch(List<SkuSpecificationSelectionPO> selections);
}

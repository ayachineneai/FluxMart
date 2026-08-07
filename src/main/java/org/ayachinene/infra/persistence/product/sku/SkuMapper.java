package org.ayachinene.infra.persistence.product.sku;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SkuMapper extends BaseMapper<SkuPO> {

    int insertBatch(List<SkuPO> skus);
}

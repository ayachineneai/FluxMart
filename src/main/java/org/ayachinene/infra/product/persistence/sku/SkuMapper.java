package org.ayachinene.infra.product.persistence.sku;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.ayachinene.app.product.domain.ProductCode;

import java.util.List;

@Mapper
public interface SkuMapper extends BaseMapper<SkuPO> {

    int insertBatch(List<SkuPO> skus);

    boolean existsByProductCode(ProductCode productCode);
}

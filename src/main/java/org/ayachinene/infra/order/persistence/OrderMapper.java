package org.ayachinene.infra.order.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.ayachinene.shared.uuid7.UUID7;

@Mapper
public interface OrderMapper extends BaseMapper<OrderPO> {

    default OrderPO selectByUserIdAndRequestKey(UUID7 userId, String requestKey) {
        return selectOne(Wrappers.<OrderPO>lambdaQuery()
                .eq(OrderPO::getUserId, userId)
                .eq(OrderPO::getRequestKey, requestKey));
    }
}

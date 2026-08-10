package org.ayachinene.infra.order.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.annotations.Mapper;
import org.ayachinene.shared.uuid7.UUID7;

import java.util.List;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItemPO> {

    int insertBatch(List<OrderItemPO> items);

    default List<OrderItemPO> selectByOrderId(UUID7 orderId) {
        return selectList(Wrappers.<OrderItemPO>lambdaQuery()
                .eq(OrderItemPO::getOrderId, orderId)
                .orderByAsc(OrderItemPO::getSortOrder));
    }
}

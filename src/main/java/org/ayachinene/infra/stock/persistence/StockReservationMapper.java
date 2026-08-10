package org.ayachinene.infra.stock.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.ayachinene.app.order.domain.OrderCode;

import java.util.List;

@Mapper
public interface StockReservationMapper extends BaseMapper<StockReservationPO> {

    List<StockReservationTarget> selectTargetsByOrderCode(OrderCode orderCode);

    int insertBatch(List<StockReservationPO> reservations);
}

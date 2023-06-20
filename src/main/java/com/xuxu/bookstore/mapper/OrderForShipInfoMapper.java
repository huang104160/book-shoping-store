package com.xuxu.bookstore.mapper;

import com.xuxu.bookstore.vo.OrderForShipInfoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderForShipInfoMapper {
    public List<OrderForShipInfoVo> findOrderForShipInfo(Map<String, Object> paramMap);

    List<Long> findShippedOrders();
}

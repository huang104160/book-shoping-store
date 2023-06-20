package com.xuxu.bookstore.service;

import com.xuxu.bookstore.dto.OrderForShipInfoDto;
import com.xuxu.bookstore.vo.OrderForShipInfoVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface OrderForShipInfoService {

    /**
     * 根据userId联表查处于发货状态(order.state = 2)的订单的图书信息
     * @param userId
     * @return
     */
    public List<OrderForShipInfoVo> getOrderForShipInfoVoList(Long userId);

    /**
     * 收货一本图书, 需要做
     * 1, 更新 shipments 的收货时间字段,
     * 2, 更新 orderItem 表的状态 为 3 // 0未付款 1已付款 2正在配送 3已签收 4正在退回
     * 3, 当一个order 对应的 orderItem 的状态都为 3  那么更新 对应的order 表的状态为 3
     */
    @Transactional
    public void gottenItem(OrderForShipInfoDto orderForShipInfoDto);


    /**
     * 收货多本图书, 需要做
     * 1, 更新 shipments 的收货时间字段,
     * 2, 更新 orderItem 表的状态 为 3 // 0未付款 1已付款 2正在配送 3已签收 4正在退回
     * 3, 当一个order 对应的 orderItem 的状态都为 3  那么更新 对应的order 表的状态为 3
     */
    @Transactional
    public void gottenItemBatch(List<Long> ids);
}

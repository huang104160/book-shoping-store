package com.xuxu.bookstore.service.impl;

import com.xuxu.bookstore.dto.OrderForShipInfoDto;
import com.xuxu.bookstore.entity.OrderItem;
import com.xuxu.bookstore.entity.ShipmentsEntity;
import com.xuxu.bookstore.mapper.OrderForShipInfoMapper;

import com.xuxu.bookstore.service.OrderForShipInfoService;
import com.xuxu.bookstore.service.OrderItemService;
import com.xuxu.bookstore.service.ShipmentsService;
import com.xuxu.bookstore.vo.OrderForShipInfoVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuxu.bookstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderForShipInfoServiceImpl implements OrderForShipInfoService {
    @Autowired
    OrderForShipInfoMapper orderForShipInfoMapper;

    @Autowired
    ShipmentsService shipmentsService;

    @Autowired
    OrderItemService orderItemService;

    @Autowired
    OrderService orderService;

    public List<OrderForShipInfoVo> getOrderForShipInfoVoList(Long userId){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("status", 2); // 0未付款 1已付款 2正在配送 3已签收 4正在退回
        paramMap.put("userId", userId);
        List<OrderForShipInfoVo> orderForShipInfoList = orderForShipInfoMapper.findOrderForShipInfo(paramMap);
        orderForShipInfoList.forEach(orderForShipInfoVo -> {
            orderForShipInfoVo.setUserId(userId);
        });
        return orderForShipInfoList;
    }

    /**
     * 收货一本图书, 需要做
     * 1, 更新 shipments 的收货时间字段,
     * 2, 更新 orderItem 表的状态 为 3 // 0未付款 1已付款 2正在配送 3已签收 4正在退回
     * 3, 当一个order 对应的 orderItem 的状态都为 3  那么更新 对应的order 表的状态为 3
     */
    @Override
    public void gottenItem(OrderForShipInfoDto orderForShipInfoDto) {
        // 1, 更新 shipments 的收货时间字段,设置逻辑删除字段
        LambdaQueryWrapper<ShipmentsEntity> qwSE = new LambdaQueryWrapper<>();
        qwSE.eq(ShipmentsEntity::getOrderItemId,orderForShipInfoDto.getId());
        ShipmentsEntity shipmentsEntity = new ShipmentsEntity();
        shipmentsEntity.setIsDeleted(1);
        shipmentsEntity.setShipmentGottenDate(LocalDateTime.now());
        shipmentsService.update(shipmentsEntity,qwSE);

        // 更新 orderItem 表的状态 为 3, 设置逻辑删除字段
        OrderItem orderItem = new OrderItem();
        orderItem.setId(orderForShipInfoDto.getId());
        orderItem.setStatus(3);
        orderItem.setIsDeleted(1);
        orderItemService.updateById(orderItem);

        // 当一个orderItem 对应的状态都为 3  那么更新 对应的order 表的状态为 3
        // 由于gottenItem 方法使用transition修饰, 上面的 更新 orderItem 表的状态 为 3 不能及时写入数据库,
        // 所以这里用异步定时任务操作进行order表更新
    }

    /**
     * 收货多本图书, 需要做
     * 1, 更新 shipments 的收货时间字段,
     * 2, 更新 orderItem 表的状态 为 3 // 0未付款 1已付款 2正在配送 3已签收 4正在退回
     * 3, 当一个order 对应的 orderItem 的状态都为 3  那么更新 对应的order 表的状态为 3
     */
    @Override
    public void gottenItemBatch(List<Long> ids) {
        //1, 更新 shipments 的收货时间字段,
        ids.stream().forEach(id->{
            ShipmentsEntity shipmentsEntity = new ShipmentsEntity();
            shipmentsEntity.setShipmentGottenDate(LocalDateTime.now());
            shipmentsEntity.setIsDeleted(1);
            LambdaQueryWrapper<ShipmentsEntity> seqw = new LambdaQueryWrapper<>();
            seqw.eq(ShipmentsEntity::getOrderItemId,id);
            shipmentsService.update(shipmentsEntity,seqw);
        });
        // 2, 更新 orderItem 表的状态 为 3 // 0未付款 1已付款 2正在配送 3已签收 4正在退回
        List<OrderItem> collect = ids.stream().map(id -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(id);
            orderItem.setStatus(3);
            orderItem.setIsDeleted(1);
            return orderItem;
        }).collect(Collectors.toList());
        orderItemService.updateBatchById(collect);

        // 3, 当一个order 对应的 orderItem 的状态都为 3  那么更新 对应的order 表的状态为 3



    }

}

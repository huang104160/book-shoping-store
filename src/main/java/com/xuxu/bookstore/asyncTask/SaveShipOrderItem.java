package com.xuxu.bookstore.asyncTask;


import com.xuxu.bookstore.entity.Order;
import com.xuxu.bookstore.entity.OrderItem;
import com.xuxu.bookstore.entity.ShipmentsEntity;
import com.xuxu.bookstore.entity.User;
import com.xuxu.bookstore.mapper.OrderMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xuxu.bookstore.service.BookService;
import com.xuxu.bookstore.service.OrderItemService;
import com.xuxu.bookstore.service.ShipmentsService;
import com.xuxu.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SaveShipOrderItem {
    @Autowired
    UserService userService;

    @Autowired
    BookService bookService;

    @Autowired
    ShipmentsService shipmentsService;

    // 会出现循环依赖 , 如果注入的是OrderService的话
    @Autowired
    OrderMapper orderMapper;
    
    @Autowired
    OrderItemService orderItemService;
    @Async
    @Transactional
    public void saveShip(Map<Long,Long> shipMap, Long userId, Long orderId){
        try {
            Thread.sleep(5000); //用户支付订单后, 等5秒更新发货信息表 同时 将订单表中的状态更新为发货状态 同时 更新所有订单详细表的状态
            //  0未付款 1已付款 2正在配送 3已签收 4正在退回, 由1 变为2
            Order order = new Order();
            order.setId(orderId);
            order.setStatus(2);
            orderMapper.updateById(order);
            
            //  更新所有订单详细表的状态
            List<OrderItem> collectOrderItem = shipMap.keySet().stream().map(key -> {
                OrderItem orderItem = new OrderItem();
                orderItem.setId(key);
                orderItem.setStatus(2);
                return orderItem;
            }).collect(Collectors.toList());
            orderItemService.updateBatchById(collectOrderItem);

            // 更新发货信息表
            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.eq("id",userId);
            User one = userService.getOne(qw);
            String sendto = one.getAddress() != null ? one.getAddress() : "未知地区";
            List<ShipmentsEntity> collect = shipMap.keySet().stream().map(key -> {
                String supplierAddr = bookService.getById(shipMap.get(key)).getSupplierAddr();
                ShipmentsEntity shipmentsEntity = new ShipmentsEntity();
                shipmentsEntity.setOrderItemId(key);
                shipmentsEntity.setSendFrom(supplierAddr);
                shipmentsEntity.setSendTo(sendto);
                shipmentsEntity.setShipmentDate(LocalDateTime.now());
                shipmentsEntity.setIsDeleted(0);
                return shipmentsEntity;
            }).collect(Collectors.toList());
            shipmentsService.saveBatch(collect);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

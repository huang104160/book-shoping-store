package com.xuxu.bookstore.service.impl;

import com.xuxu.bookstore.asyncTask.SaveShipOrderItem;
import com.xuxu.bookstore.entity.Order;
import com.xuxu.bookstore.entity.OrderItem;
import com.xuxu.bookstore.entity.ShoppingCart;
import com.xuxu.bookstore.mapper.OrderMapper;
import com.xuxu.bookstore.service.OrderItemService;
import com.xuxu.bookstore.service.OrderService;
import com.xuxu.bookstore.service.ShoppingCartService;
import com.xuxu.bookstore.utils.GenerateId;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ShoppingCartService shoppingCartService;


    @Autowired
    private SaveShipOrderItem saveShipOrderItem;

    @Override
    public void saveOrderWithItems(List<ShoppingCart> cartItems, Long userId) {
        // 结账的购物项id
        List<Long> ids = new ArrayList<>();


        // 物流的购物项id 和 bookid
        Map<Long, Long> shipMap = new HashMap<>();

        // 保存订单信息
        Order order = new Order();
        // 自动生成id
        Long orderId = GenerateId.getId();
        order.setId(orderId);
        // 设置userId
        order.setUserId(userId);
        // 总数量
        int totalCount = 0;
        // 总金额(使用BigDecimal对象计算浮点数)
        BigDecimal bigDecimal = new BigDecimal(0);

        for (ShoppingCart cartItem : cartItems) {
            // 计算总数量
            totalCount += cartItem.getNumber();
            // 计算总价格(对浮点数进行处理)
            bigDecimal = bigDecimal.add(BigDecimal.valueOf(cartItem.getPrice()).multiply(BigDecimal.valueOf(cartItem.getNumber())));

        }
        order.setTotalCount(totalCount);
        double totalPrice = bigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
        order.setTotalAmount(totalPrice);
        // 0未付款 1已付款 2正在配送 3已签收 4正在退回
        order.setStatus(1);
        this.save(order);

        for (ShoppingCart cartItem : cartItems) {
//            // 计算总数量
//            totalCount += cartItem.getNumber();
//            // 计算总价格(对浮点数进行处理)
//            bigDecimal = bigDecimal.add(BigDecimal.valueOf(cartItem.getPrice()).multiply(BigDecimal.valueOf(cartItem.getNumber())));
            // 保存订单项
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(orderId);
            orderItem.setBookId(cartItem.getId());
            orderItem.setNumber(cartItem.getNumber());
            orderItem.setStatus(1);
            orderItem.setIsDeleted(0);
            orderItemService.save(orderItem);
            // 保存已物流的购物项id
            shipMap.put(orderItem.getId(), orderItem.getBookId());
            // 保存已结账的购物项id
            ids.add(cartItem.getId());
        }


        // 清空已结账的购物项
        shoppingCartService.removeByIds(ids);
        saveShipOrderItem.saveShip(shipMap, userId, order.getId());
    }


}

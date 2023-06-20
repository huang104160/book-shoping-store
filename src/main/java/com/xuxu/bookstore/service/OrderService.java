package com.xuxu.bookstore.service;

import com.xuxu.bookstore.entity.Order;
import com.xuxu.bookstore.entity.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface OrderService extends IService<Order> {
    /**
     * 保存订单及其订单项
     * @param cartItems
     * @param userId
     */
    @Transactional
    void saveOrderWithItems(List<ShoppingCart> cartItems, Long userId);


}

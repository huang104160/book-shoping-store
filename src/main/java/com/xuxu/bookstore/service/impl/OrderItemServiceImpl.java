package com.xuxu.bookstore.service.impl;

import com.xuxu.bookstore.entity.OrderItem;
import com.xuxu.bookstore.mapper.OrderItemMapper;
import com.xuxu.bookstore.service.OrderItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem> implements OrderItemService {
}

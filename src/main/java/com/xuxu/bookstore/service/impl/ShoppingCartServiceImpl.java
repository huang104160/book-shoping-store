package com.xuxu.bookstore.service.impl;

import com.xuxu.bookstore.entity.ShoppingCart;
import com.xuxu.bookstore.mapper.ShoppingCartMapper;
import com.xuxu.bookstore.service.ShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}

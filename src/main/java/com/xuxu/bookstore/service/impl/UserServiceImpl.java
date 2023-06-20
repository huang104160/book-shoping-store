package com.xuxu.bookstore.service.impl;

import com.xuxu.bookstore.entity.User;
import com.xuxu.bookstore.mapper.UserMapper;
import com.xuxu.bookstore.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}

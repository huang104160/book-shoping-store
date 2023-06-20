package com.xuxu.bookstore.service.impl;

import com.xuxu.bookstore.entity.Book;
import com.xuxu.bookstore.mapper.BookMapper;
import com.xuxu.bookstore.service.BookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {
}

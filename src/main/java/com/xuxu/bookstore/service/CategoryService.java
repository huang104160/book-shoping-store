package com.xuxu.bookstore.service;

import com.xuxu.bookstore.dto.CategoryDto;
import com.xuxu.bookstore.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

public interface CategoryService extends IService<Category> {
    CategoryDto getCategoryWithBooks(Long id);
}

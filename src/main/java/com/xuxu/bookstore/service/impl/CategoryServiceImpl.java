package com.xuxu.bookstore.service.impl;

import com.xuxu.bookstore.dto.CategoryDto;
import com.xuxu.bookstore.entity.Book;
import com.xuxu.bookstore.entity.Category;
import com.xuxu.bookstore.mapper.CategoryMapper;
import com.xuxu.bookstore.service.BookService;
import com.xuxu.bookstore.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private BookService bookService;

    /**
     * 根据分类 id 查询分类信息及其对应的所有图书信息
     *
     * @param id
     * @return
     */
    @Override
    public CategoryDto getCategoryWithBooks(Long id) {
        // 1. 创建 categoryDto 对象
        CategoryDto categoryDto = new CategoryDto();
        // 2. 根据分类 id 查询分类信息
        Category category = this.getById(id);
        // 3. 将查询的分类信息复制到 categoryDto 对象中
        BeanUtils.copyProperties(category, categoryDto);
        // 4. 根据分类 id 查询所有的图书信息
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Book::getCategoryId, id);
        List<Book> bookList = bookService.list(queryWrapper);
        // 5. 将查询的图书信息封装到 categoryDto 对象中
        categoryDto.setBookList(bookList);
        // 6. 返回封装好的 categoryDto 对象
        return categoryDto;
    }
}

package com.xuxu.bookstore.dto;

import com.xuxu.bookstore.entity.Book;
import com.xuxu.bookstore.entity.Category;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDto extends Category {
    List<Book> bookList;
}

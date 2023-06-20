package com.xuxu.bookstore.controller;

import com.xuxu.bookstore.common.R;
import com.xuxu.bookstore.entity.Book;
import com.xuxu.bookstore.service.BookService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 图书信息管理控制器
 */

@Api(tags = "图书信息接口")
@Slf4j
@RestController
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * 新增图书
     *
     * @param book
     * @return
     */
    @CacheEvict(value = "bookCache", allEntries = true)
    @ApiOperation("保存图书")
    @PostMapping
    public R<String> saveAndEdit(@RequestBody Book book, @RequestParam String action) {
        if ("add".equalsIgnoreCase(action)) {
            bookService.save(book);
            return R.successMsg("保存图书成功");
        } else if ("edit".equalsIgnoreCase(action)) {
            bookService.updateById(book);
            return R.successMsg("修改图书成功");
        }
        return R.error("请指定要做的操作【add or edit】");
    }

    /**
     * 根据 id 删除一本或批量删除多本图书
     *
     * @param ids
     * @return
     */
    @CacheEvict(value = "bookCache", allEntries = true)
    @ApiOperation("删除图书")
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        bookService.removeByIds(ids);
        return R.successMsg("删除图书成功");
    }

    /**
     * 根据 id 修改图书信息
     *
     * @param book
     * @return
     */
    @CacheEvict(value = "bookCache", allEntries = true)
    @ApiOperation("更新图书")
    @PutMapping
    public R<String> update(@RequestBody Book book) {
        bookService.updateById(book);
        return R.successMsg("修改图书信息成功");
    }

    /**
     * 根据 id 修改一个或批量修改多个图书状态
     *
     * @param status
     * @param ids
     * @return
     */
    @CacheEvict(value = "bookCache", allEntries = true)
    @ApiOperation("修改图书状态")
    @PutMapping("/status/{status}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态[1正常 0禁用]", dataTypeClass = String.class),
            @ApiImplicitParam(name = "ids", value = "id列表", dataTypeClass = String.class)
    })
    public R<String> status(@PathVariable("status") Integer status, @RequestParam List<Long> ids) {
        LambdaUpdateWrapper<Book> updateWrapper = new LambdaUpdateWrapper<>();
        // SQL: update book set status = status where id in (ids);
        updateWrapper.in(Book::getId, ids);
        updateWrapper.set(Book::getStatus, status);
        bookService.update(updateWrapper);
        return R.success("修改图书状态成功");
    }

    /**
     * 根据 id 获取图书信息
     *
     * @param id
     * @return
     */
    @ApiOperation("根据id获取图书信息")
    @GetMapping("/{id}")
    public R<Book> getById(@PathVariable("id") Long id) {
        Book book = bookService.getById(id);
        return R.success(book);
    }


//    /**
//     * 分页和书名查询图书信息
//     *
//     * @param page     当前页数
//     * @param pageSize 一页的数据量
//     * @param bookName 部分书名
//     * @return
//     */
//    @Cacheable(value = "bookCache", key = "'book_' + #page + '_' + #pageSize + '_' + #bookName")
//    @ApiOperation("分页查询图书信息")
//    @GetMapping("/page/{page}/{pageSize}")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "page", value = "页码", required = true, dataTypeClass = String.class),
//            @ApiImplicitParam(name = "pageSize", value = "每页数据量", required = true, dataTypeClass = String.class),
//            @ApiImplicitParam(name = "bookName", value = "图书名", dataTypeClass = String.class)
//    })
//    public R<Page<Book>> page(
//            @PathVariable("page") Integer page,
//            @PathVariable("pageSize") Integer pageSize,
//            String bookName,
//            String type
//    ) {
//        // 最多返回30条数据
//        if (pageSize > 30) {
//            pageSize = 30;
//        }
//        Page<Book> pageInfo = new Page<>(page, pageSize);
//        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
//        // SQL: select * from book where name like %bookName% order by sort asc, update_time desc;
//        queryWrapper.like(bookName != null, Book::getName, bookName);
//        if ("low".equals(type)) {
//            queryWrapper.orderByAsc(Book::getPrice);
//        } else {
//            queryWrapper.orderByDesc(Book::getUpdateTime);
//        }
//        bookService.page(pageInfo, queryWrapper);
//        return R.success(pageInfo);
//    }

    /**
     * 分页查询图书信息
     *
     * @param page     当前页数
     * @param pageSize 一页的数据量
     * @return
     */
    @Cacheable(value = "bookCache", key = "'book_' + #page + '_' + #pageSize + '_' + #bookName")
    @ApiOperation("分页查询图书信息")
    @GetMapping("/page/{page}/{pageSize}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "pageSize", value = "每页数据量", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "bookName", value = "图书名", dataTypeClass = String.class)
    })
    public R<Page<Book>> page(
            @PathVariable("page") Integer page,
            @PathVariable("pageSize") Integer pageSize,
            @RequestParam(value = "random",required = false) String random,
            @RequestParam(name="bookName",required = false) String bookName,
            @RequestParam(name="type",required = false) String type
    ) {
        // 最多返回30条数据
        if (pageSize > 30) {
            pageSize = 30;
        }
        Page<Book> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        // SQL: select * from book where name like '%了%' order by price asc, update_time desc LIMIT 0,10
        queryWrapper.like(bookName != null, Book::getName, bookName);
        if ("low".equals(type)) {
            queryWrapper.orderByAsc(Book::getPrice);
        } else {
            queryWrapper.orderByDesc(Book::getUpdateTime);
        }
        bookService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @GetMapping("/search/{page}/{pageSize}")
    public R<Page<Book>> search(
            String key,
            @PathVariable("page") Integer page,
            @PathVariable("pageSize") Integer pageSize
    ) {
        key = key.trim();
        if (StringUtils.isNotBlank(key)) {
            // 1. 创建分页对象
            Page<Book> pageInfo = new Page<>(page, pageSize);
            // 2. 构造查询对象
            LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
            // SQL: select * from book where name like key or author like key or publisher like key or description like key;
            // 3. 根据关键字模糊查询 name、author、publisher
            queryWrapper.like(Book::getName, key).or().like(Book::getAuthor, key).or().like(Book::getPublisher, key).or().like(Book::getDescription, key);
            queryWrapper.eq(Book::getStatus, 1);
            // 4. 分页查询
            bookService.page(pageInfo, queryWrapper);
            return R.success(pageInfo);
        }
        return R.success(new Page<>());
    }

}

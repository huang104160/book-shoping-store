package com.xuxu.bookstore.controller;

import com.xuxu.bookstore.common.R;
import com.xuxu.bookstore.dto.CategoryDto;
import com.xuxu.bookstore.entity.Category;
import com.xuxu.bookstore.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 图书分类控制器
 */

@Api(tags = "图书分类接口")
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     *
     * @param category
     * @return
     */
    @CacheEvict(value = "categoryCache", allEntries = true)
    @ApiOperation("新增分类")
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        // 设置初始状态为正常
        category.setStatus(1);
        categoryService.save(category);
        return R.successMsg("新增分类成功");
    }

    /**
     * 根据 id 删除一个或批量删除多个分类
     *
     * @param ids
     * @return
     */
    @CacheEvict(value = "categoryCache", allEntries = true)
    @ApiOperation("删除分类")
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        categoryService.removeByIds(ids);
        return R.successMsg("删除分类成功");
    }

    /**
     * 根据 id 修改分类信息
     *
     * @param category
     * @return
     */
    @CacheEvict(value = "categoryCache", allEntries = true)
    @ApiOperation("更新分类")
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.successMsg("修改分类成功");
    }

    /**
     * 根据 id 修改一个或批量修改多个用分类状态
     *
     * @param status
     * @param ids
     * @return
     */
    @CacheEvict(value = "categoryCache", allEntries = true)
    @ApiOperation("修改分类状态")
    @PutMapping("/status/{status}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "status", value = "状态[1正常 0禁用]", dataTypeClass = String.class),
            @ApiImplicitParam(name = "ids", value = "id列表", dataTypeClass = String.class)
    })
    public R<String> status(@PathVariable("status") Integer status, @RequestParam List<Long> ids) {
        LambdaUpdateWrapper<Category> updateWrapper = new LambdaUpdateWrapper<>();
        // SQL: update category set status = status where id in (ids);
        updateWrapper.in(Category::getId, ids);
        updateWrapper.set(Category::getStatus, status);
        categoryService.update(updateWrapper);
        return R.successMsg("修改用户状态成功");
    }

    /**
     * 根据 id 获取分类信息
     *
     * @param id
     * @return
     */
    @ApiOperation("根据分类id获取分类信息及其图书信息")
    @GetMapping("/{id}")
//    @Cacheable(value = "bookCache", key = "'book_' + #page + '_' + #pageSize + '_' + #bookName")
    @Cacheable(value = "categoryCache", key = "'category_' + #id ")
    public R<CategoryDto> getById(@PathVariable("id") Long id) {
        CategoryDto categoryDto = categoryService.getCategoryWithBooks(id);
        return R.success(categoryDto);
    }

    /**
     * 获取所有图书分类
     *
     * @return
     */
    @Cacheable(value = "categoryCache", key = "'category'")
    @ApiOperation("获取所有图书分类")
    @GetMapping("/list")
    @ApiImplicitParam(name = "name", value = "图书分类名", required = false, dataTypeClass = String.class)
    public R<List<Category>> list(String name) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // SQL: select * from category [where name like name] order by sort, update_time desc;
        queryWrapper.like(name != null, Category::getName, name);
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        queryWrapper.eq(Category::getStatus, 1);
        List<Category> categoryList = categoryService.list(queryWrapper);
        return R.success(categoryList);
    }

    @Cacheable(value = "categoryCache", key = "'category_' + #page + '_' + #pageSize")
    @ApiOperation("分页查询分类信息")
    @GetMapping("/page/{page}/{pageSize}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "pageSize", value = "每页数据量", required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "categoryName", value = "分类名", dataTypeClass = String.class)
    })
    public R<Page<Category>> page(
            @PathVariable("page") Integer page,
            @PathVariable("pageSize") Integer pageSize,
            String categoryName
    ) {
        // 最多返回30条数据
        if (pageSize > 30) {
            pageSize = 30;
        }
        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // SQL: select * from category where name like %categoryName% order by sort asc, update_time desc;
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        queryWrapper.like(categoryName != null, Category::getName, categoryName);
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }
}

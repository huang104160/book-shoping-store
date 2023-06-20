package com.xuxu.bookstore.controller;

import com.xuxu.bookstore.common.R;
import com.xuxu.bookstore.entity.ShoppingCart;
import com.xuxu.bookstore.entity.User;
import com.xuxu.bookstore.service.ShoppingCartService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@Api(tags = "购物车接口")
@RequestMapping("/cart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加图书到购物车
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping
    @ApiOperation("添加图书到购物车")
    public R<String> save(@RequestBody ShoppingCart shoppingCart) {
        log.info("shopping cart: {}", shoppingCart);
        shoppingCart.setNumber(1);
        shoppingCartService.save(shoppingCart);
        return R.successMsg("新增图书成功");
    }

    /**
     * 删除购物车中图书
     *
     * @param ids 要删除图书的id列表
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除购物项")
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("要从购物车中删除的图书id列表 = {}", ids);
        // SQL: delete from shopping_cart where id in (ids);
        shoppingCartService.removeByIds(ids);
        return R.successMsg("删除成功");
    }

    /**
     * 修改购物车中图书的数量
     *
     * @param shoppingCart
     * @return
     */
    @PutMapping
    @ApiOperation("修改图书的购买数量")
    public R<String> updateNumber(@RequestBody ShoppingCart shoppingCart) {
        if (shoppingCart.getNumber() < 1) {
            return R.error("图书数量小于1，修改失败");
        }
        shoppingCartService.updateById(shoppingCart);
        return R.success("修改图书购买数量成功");
    }

    /**
     * 展示用户的购物车
     *
     * @param user
     * @return
     */
    @ApiOperation("展示用户的购物车列表")
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(User user) {
        // 1. 获取用户id
        Long userId = user.getId();
        // 2. 根据用户 id 从 shopping_cart 表中查找属于用户的商品列表
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        queryWrapper.orderByDesc(ShoppingCart::getUpdateTime);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(queryWrapper);
        // 3. 返回商品列列表
        return R.success(shoppingCartList);
    }
}

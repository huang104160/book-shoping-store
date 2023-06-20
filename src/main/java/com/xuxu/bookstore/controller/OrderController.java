package com.xuxu.bookstore.controller;

import com.xuxu.bookstore.common.R;
import com.xuxu.bookstore.entity.Order;
import com.xuxu.bookstore.entity.ShoppingCart;
import com.xuxu.bookstore.service.OrderItemService;
import com.xuxu.bookstore.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 订单表
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    OrderItemService orderItemService;

    /**
     * 结算购物车提交订单
     *
     * @param cartItems 购物车列表
     * @return
     */
    @PostMapping
    public R<String> settleAccounts(@RequestBody List<ShoppingCart> cartItems, Long userId) {
        orderService.saveOrderWithItems(cartItems, userId);
        // 生成订单项信息
        return R.successMsg("结算成功!");
    }

    /**
     * 分页查询订单信息
     *
     * @param page      页码
     * @param pageSize  页码数量
     * @param orderId   订单Id
     * @param beginTime 开始时间
     * @param endTime   结束时间
     * @return
     */
    @GetMapping("/page/{page}/{pageSize}")
    public R<Page<Order>> page(
            @PathVariable("page") int page,
            @PathVariable("pageSize") int pageSize,
            Long orderId,
            String beginTime,
            String endTime
    ) {
        Page<Order> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Order> queryWrapper = new LambdaQueryWrapper<>();
        // 根据订单号查询
        queryWrapper.eq(orderId != null, Order::getId, orderId);
        // 根据开始时间和结束时间查询
        queryWrapper.ge(beginTime != null, Order::getCreateTime, beginTime);
        queryWrapper.le(endTime != null, Order::getCreateTime, endTime);
        orderService.page(pageInfo, queryWrapper);
        // 查询订单信息
        return R.success(pageInfo);
    }
}

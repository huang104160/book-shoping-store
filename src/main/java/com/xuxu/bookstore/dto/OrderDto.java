package com.xuxu.bookstore.dto;

import com.xuxu.bookstore.entity.Order;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderDto extends Order {

    @ApiModelProperty("订单中详情的订单项列表")
    private Page<Order> orderList;

    private String username;

    private String phone;

    private String address;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;
}

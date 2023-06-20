package com.xuxu.bookstore.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("订单详情表")
@TableName("order_item")
public class OrderItem implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("订单id")
    private Long orderId;

    @ApiModelProperty("图书id")
    private Long bookId;

    @ApiModelProperty("当前商品购买数量")
    private Integer number;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("状态 [0未付款 1已付款 2正在配送 3已签收 4正在退回]")
    private Integer status;


    private Integer isDeleted;
}

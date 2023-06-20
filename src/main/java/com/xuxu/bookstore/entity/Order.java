package com.xuxu.bookstore.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("订单表")
// 注意: 因为order是关键字，所以需要使用 `` 反引号括起来
@TableName("`order`")
@JsonIgnoreProperties(value = {"isDeleted"})
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    // 不使用主键生成策略
    @TableId(type = IdType.NONE)
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户名")
    private Long userId;

    @ApiModelProperty("总数量")
    private Integer totalCount;

    // 你这里用double搞什么, mybatisplus插入转不了, 用Double
    @ApiModelProperty("总价")
    private Double totalAmount;
//    private double totalAmount;

    @ApiModelProperty("状态 [0未付款 1已付款 2正在配送 3已签收 4正在退回]")
    private Integer status;

    @ApiModelProperty("创建时间 ")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间 ")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer isDeleted;
}

package com.xuxu.bookstore.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@ApiModel("购物车")
@TableName("shopping_cart")
public class ShoppingCart implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("图书名")
    private String name;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("出版社")
    private String publisher;

    @ApiModelProperty("价格")
    private Double price;

    @ApiModelProperty("图片路径")
    private String imageName;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("数量")
    private Integer number;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

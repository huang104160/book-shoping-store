package com.xuxu.bookstore.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("book")
@JsonIgnoreProperties(value = {"isDeleted"})
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty(value = "分类id", required = true)
    private Long categoryId;

    @ApiModelProperty(value = "图书名", required = true)
    private String name;

    @ApiModelProperty(value = "作者名", required = true)
    private String author;

    @ApiModelProperty(value = "出版社", required = true)
    private String publisher;

    @ApiModelProperty(value = "图书价格", required = true)
    private Double price;

    @ApiModelProperty("图片路径")
    private String imageName;

    @ApiModelProperty("图书描述")
    private String description;

    @ApiModelProperty("状态 1正常 0禁止销售")
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;


    @ApiModelProperty("供货商id")
    private Integer supplierId;

    @ApiModelProperty("供货商名称")
    private String supplierName;

    @ApiModelProperty("供货商地址")
    private String supplierAddr;

    @TableLogic
    private Integer isDeleted;
}

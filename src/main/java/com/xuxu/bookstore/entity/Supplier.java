package com.xuxu.bookstore.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("suppliers")
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    private int id;
    @ApiModelProperty(value = "供货商名称",required = true)
    private String name;
    @ApiModelProperty(value = "供货商地址",required = true)
    private String address;
    @ApiModelProperty(value = "供货商联系电话",required = true)
    private String phone;
}

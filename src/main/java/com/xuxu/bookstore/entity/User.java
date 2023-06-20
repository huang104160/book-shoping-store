package com.xuxu.bookstore.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("user")
@JsonIgnoreProperties(value = {"isDeleted", "isAdmin"})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    @ApiModelProperty(value = "密码", required = true)
    // 设置该字段只能写入 不能读取返回给前端。解决了SpringMVC能接受password参数，并且返回实体类给前端时不返回该字段
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ApiModelProperty(value = "手机号", required = true)
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("是否禁用 (1正常 0禁用)")
    private int status;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @TableLogic
    // 作用：在json序列化时将java bean中的一些属性忽略掉，序列化和反序列化都受影响。与JsonIgnoreProperties类注解一样作用
    // @JsonIgnore
    private Integer isDeleted;

    private String isAdmin;
}

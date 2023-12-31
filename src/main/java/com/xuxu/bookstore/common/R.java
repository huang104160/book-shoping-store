package com.xuxu.bookstore.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回结果类，服务端响应的数据最终都会封装成此对象
 *
 * @param <T>
 */
@Data
@ApiModel("返回结果")
public class R<T> implements Serializable {

    @ApiModelProperty("编码")
    private Integer code; // 编码：1成功，0和其它数字为失败

    @ApiModelProperty("数据")
    private T data; // 数据

    @ApiModelProperty("额外信息")
    private String msg; // 错误信息

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> successMsg(String message) {
        R<T> r = new R<T>();
        r.code = 1;
        r.msg = message;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R<T> r = new R<>();
        r.msg = msg;
        r.code = 0;
        return r;
    }
}

package com.xuxu.bookstore.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器
 */
// 异常处理类注解，拦截产生异常的有RestController，Controller注解的类
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕获其他异常
     */
    @ExceptionHandler(Exception.class)
    public R<String> exceptionHandler(Exception ex) {
        log.error(ex.getMessage());
        return R.error("未知错误");
    }

    /**
     * 捕获重复往数据库插入用户名的异常
     *
     * @param ex 捕获到的异常处理对象
     * @return
     */
    @ExceptionHandler(SQLException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error(ex.getMessage());

        if (ex.getMessage().contains("Duplicate entry") && ex.getMessage().contains("unique_name_key")) {
            String employeeName = ex.getMessage().split(" ")[2];
            return R.error(employeeName + "用户已存在");
        }

        if (ex.getMessage().contains("Duplicate entry") && ex.getMessage().contains("unique_phone_key")) {
            String phone = ex.getMessage().split(" ")[2];
            return R.error(phone + "手机号已存在");
        }

        if (ex.getMessage().contains("`category` (`id`)")) {
            return R.error("请选择正常的图书分类");
        }

        if (ex.getMessage().contains("Duplicate entry") && ex.getMessage().contains("shopping_cart.PRIMARY")) {
            return R.error("该商品已在购物车，请勿重复添加");
        }

        return R.error("未知错误");
    }

    /**
     * 捕获自定义异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex) {
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }
}

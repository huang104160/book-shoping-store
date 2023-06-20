package com.xuxu.bookstore.common;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前用户的id
 */
public class BaseContext {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 往ThreadLocal对象中添加id
     *
     * @param id 当前登录用户的id
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 往ThreadLocal对象中获取id
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }
}

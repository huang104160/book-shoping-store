package com.xuxu.bookstore.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderForShipInfoDto {
    /**
     * 订单详情id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 书名
     */
    private String name;

    /**
     * 作者
     */
    private String author;

    /**
     * 出版社
     */
    private String publisher;

    /**
     * 价格
     */
    private Double price;

    /**
     * 书的封面图片
     */
    private String imageName;

    /**
     * 书的描述
     */
    private String description;

    /**
     * 书的数量
     */
    private Integer number;

    /**
     * 物流创建日期
     */
    private LocalDateTime createTime;

    /**
     * 收货日期
     */
    private LocalDateTime updateTime;

    /**
     * 发货地址
     */
    private String sendfrom;

    /**
     * 收货地址
     */
    private String sendto;

    /**
     * 供货商
     */
    private String supplier;
}

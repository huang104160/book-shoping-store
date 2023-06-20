package com.xuxu.bookstore.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 发货表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2023-06-16 11:35:07
 */
@Data
@TableName("shipments")
public class ShipmentsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 发货ID
	 */
	@TableId
	private Long id;
	/**
	 * 发货日期
	 */
	// 创建异步线程进行数据插入, 完成支付后的5s将订单插入到发货信息表中
	private LocalDateTime shipmentDate;


	/**
	 * 收货日期
	 */
	// 用户点击收货时更新此字段
	private LocalDateTime shipmentGottenDate;
	/**
	 * 订单详情ID
	 */
	private Long orderItemId;
	/**
	 * 发货地
	 */
	private String sendFrom;
	/**
	 * 收货地
	 */
	private String sendTo;

	/**
	 * 逻辑删除字段 , 不用mybatis-plus的, 自己实现吧
	 */
	private Integer isDeleted;

}

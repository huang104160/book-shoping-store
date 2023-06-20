package com.xuxu.bookstore.service;

import com.xuxu.bookstore.entity.ShipmentsEntity;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


import java.util.Map;

/**
 * 发货表
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2023-06-16 11:35:07
 */
public interface ShipmentsService extends IService<ShipmentsEntity> {

    IPage<ShipmentsEntity> queryPage(Map<String, Object> params);
}


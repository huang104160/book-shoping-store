package com.xuxu.bookstore.service.impl;

import com.xuxu.bookstore.entity.ShipmentsEntity;
import com.xuxu.bookstore.mapper.ShipmentsMapper;
import com.xuxu.bookstore.service.ShipmentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.Map;

import com.baomidou.mybatisplus.core.metadata.IPage;




@Service("shipmentsService")
public class ShipmentsServiceImpl extends ServiceImpl<ShipmentsMapper, ShipmentsEntity> implements ShipmentsService {

    @Override
    public IPage<ShipmentsEntity> queryPage(Map<String, Object> params) {
//        IPage<ShipmentsEntity> page = this.page(
//                new Query<ShipmentsEntity>().getPage(params),
//                new QueryWrapper<ShipmentsEntity>()
//        );
        // TODO
        IPage<ShipmentsEntity> page  = null;
        return page;
    }

}
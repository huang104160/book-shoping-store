package com.xuxu.bookstore.controller;

import com.xuxu.bookstore.common.R;
import com.xuxu.bookstore.dto.OrderForShipInfoDto;
import com.xuxu.bookstore.service.OrderForShipInfoService;
import com.xuxu.bookstore.vo.OrderForShipInfoVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/orderShip")
public class OrderForShipInfoController {

    @Autowired
    OrderForShipInfoService orderForShipInfoService;
    @GetMapping("/list")
    public R<List<OrderForShipInfoVo>> getOrderForShipInfo(@RequestParam("id") Long userId){
        List<OrderForShipInfoVo> list = orderForShipInfoService.getOrderForShipInfoVoList(userId);
        return R.success(list);
    }


    /**
     * 对订单中的单个图书收货
     *
     * @param orderForShipInfoDto
     * @return
     */
    @PostMapping("/gottenOne")
    @ApiOperation("对订单中的单个图书收货")
    public R<String> gottenItem(@RequestBody OrderForShipInfoDto orderForShipInfoDto) {
        log.info("orderForShipInfoDto: {}", orderForShipInfoDto);
        orderForShipInfoService.gottenItem(orderForShipInfoDto);
        return R.successMsg("收货图书成功");
    }

    /**
     * 对订单中的多个个图书收货
     *
     * @param ids 要收货图书的id列表
     * @return
     */
    @PostMapping("/gottenBatch")
    @ApiOperation("对订单中的多个个图书收货")
    public R<String> gottenItemBatch(@RequestParam List<Long> ids) {
        log.info("要从订单中的图书进行收货id列表 = {}", ids);
        orderForShipInfoService.gottenItemBatch(ids);
        return R.successMsg("删除成功");
    }
}

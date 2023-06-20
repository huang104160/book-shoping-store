package com.xuxu.bookstore.asyncTask;

import com.xuxu.bookstore.entity.Order;
import com.xuxu.bookstore.mapper.OrderForShipInfoMapper;
import com.xuxu.bookstore.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
public class ScheduleTask {
    @Autowired
    OrderForShipInfoMapper orderForShipInfoMapper;

    @Autowired
    OrderService orderService;


    @Scheduled(cron = "0 0/5 * * * ?") // 每五分钟执行一次, 当一个order 对应的 orderItem 的状态都为 3  那么更新 对应的order 表的状态为 3
    @Transactional
    public void scheduledTaskOrder(){
        List<Long> shippedOrders = orderForShipInfoMapper.findShippedOrders();
        if(shippedOrders!=null && shippedOrders.size()>=1){
            Order order = new Order();
            order.setStatus(3);
            LambdaQueryWrapper<Order> oqw = new LambdaQueryWrapper<>();
            oqw.in(Order::getId,shippedOrders);
            boolean update = orderService.update(order, oqw);
            if(update){
                log.info("更新了order表中的{}条数据",shippedOrders.size());
            }
        }else {
            log.info("没有什么要更新的");
        }

    }
}

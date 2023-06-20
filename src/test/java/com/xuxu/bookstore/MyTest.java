package com.xuxu.bookstore;

import com.xuxu.bookstore.mapper.OrderForShipInfoMapper;
import com.xuxu.bookstore.vo.OrderForShipInfoVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTest {

    @Autowired
    OrderForShipInfoMapper orderForShipInfoMapper;

    @Test
    public void test1(){ // 返回订单信息给用户看
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("status", 2);
        paramMap.put("userId", 1669170449545728002l);
        List<OrderForShipInfoVo> orderList = orderForShipInfoMapper.findOrderForShipInfo(paramMap);
        orderList.forEach(System.out::println);
    }

    @Test
    public void test2(){ // 查询order的orderItem是不是已经全都收货了,返回全收货的orderid
        List<Long> shippedOrders = orderForShipInfoMapper.findShippedOrders();
        String s = Arrays.toString(shippedOrders.toArray());

        System.out.println(s);
    }
    @Value("#{'${userRole.adminIds}'.split(',')}") // 获取配置属性并以','分割为列表
    private Set<String> adminIds;
    @Test
    public void test3(){
        System.out.println(adminIds.contains("1521294872330391556"));
        adminIds.forEach(System.out::println);
    }
}

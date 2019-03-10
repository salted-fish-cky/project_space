package com.cky.service.impl;

import com.cky.dto.OrderDTO;
import com.cky.pojo.OrderDetail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceImplTest {

    @Autowired
    private OrderServiceImpl orderService;

    private  final String BUYER_OPENID = "abc111";
    private  final String ORDER_ID = "1545386633115801747";

    @Test
    public void create() {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setBuyerName("李四");
        orderDTO.setBuyerAddress("北京");
        orderDTO.setBuyerPhone("1234");
        orderDTO.setBuyerOpenid(BUYER_OPENID);
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setProductId("0812e83399914dc89f7e57e6f911e3cb");
        orderDetail.setProductQuantity(2);

        OrderDetail orderDetail1 = new OrderDetail();
        orderDetail1.setProductId("afc171881d78419a950f23ed81b6e74f");
        orderDetail1.setProductQuantity(2);
        ArrayList<OrderDetail> list = new ArrayList<>();
        list.add(orderDetail);
        list.add(orderDetail1);
        orderDTO.setOrderDetailList(list);

        orderService.create(orderDTO);
    }

    @Test
    public void findOne() {
    }

    @Test
    public void findList() {
        PageRequest pageRequest = new PageRequest(0, 2);
        Page<OrderDTO> list = orderService.findList(BUYER_OPENID, pageRequest);
        System.out.println(list);
    }

    @Test
    public void cancel() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.cancel(orderDTO);

    }

    @Test
    public void finished() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.finished(orderDTO);
    }

    @Test
    public void paid() {
        OrderDTO orderDTO = orderService.findOne(ORDER_ID);
        OrderDTO result = orderService.paid(orderDTO);
    }
}
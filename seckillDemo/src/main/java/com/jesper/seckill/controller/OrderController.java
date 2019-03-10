package com.jesper.seckill.controller;

import com.jesper.seckill.bean.OrderInfo;
import com.jesper.seckill.bean.User;
import com.jesper.seckill.redis.RedisService;
import com.jesper.seckill.result.CodeMsg;
import com.jesper.seckill.result.Result;
import com.jesper.seckill.service.GoodsService;
import com.jesper.seckill.service.OrderService;
import com.jesper.seckill.service.UserService;
import com.jesper.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by jiangyunxiong on 2018/5/28.
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    UserService userService;

    @Autowired
    RedisService redisService;

    @Autowired
    OrderService orderService;

    @Autowired
    GoodsService goodsService;

    @RequestMapping("/detail")
    public String info(Model model, HttpServletRequest request) {
        Long orderId = (Long) request.getAttribute("orderId");
        User user = (User) request.getAttribute("user");
        if(user == null) {
//            return Result.error(CodeMsg.SESSION_ERROR);
            model.addAttribute("errmsg",Result.error(CodeMsg.SESSION_ERROR));
            return "seckill_fail";
        }
        System.out.println(orderId);
        OrderInfo order = orderService.getOrderById(orderId);
        if(order == null) {
//            return Result.error(CodeMsg.ORDER_NOT_EXIST);
            model.addAttribute("errmsg",Result.error(CodeMsg.ORDER_NOT_EXIST));
            return "seckill_fail";
        }
        long goodsId = order.getGoodsId();
        GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
//        OrderDetailVo vo = new OrderDetailVo();
//        vo.setOrder(order);
//        vo.setGoods(goods);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(orderId);
        orderInfo.setUserId(user.getId());
        orderInfo.setGoodsId(goodsId);
        orderInfo.setGoodsName(goods.getGoodsName());
        orderInfo.setGoodsPrice(goods.getSeckillPrice());
        orderInfo.setGoodsCount(order.getGoodsCount());
        orderInfo.setOrderChannel(order.getOrderChannel());
        orderInfo.setStatus(0);
        orderInfo.setCreateDate(order.getCreateDate());
        orderInfo.setDeliveryAddrId(order.getDeliveryAddrId());
        orderInfo.setPayDate(order.getPayDate());
        model.addAttribute("orderInfo",orderInfo);
        model.addAttribute("goods",goods);
        return "order_detail";
    }

}

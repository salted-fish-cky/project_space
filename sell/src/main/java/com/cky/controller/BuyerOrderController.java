package com.cky.controller;

import com.cky.VO.ResultVO;
import com.cky.converter.OrderForm2OrderDTOConverter;
import com.cky.dto.OrderDTO;
import com.cky.enums.ResultEnum;
import com.cky.exception.SellException;
import com.cky.form.OrderForm;
import com.cky.service.BuyerService;
import com.cky.service.OrderService;
import com.cky.utils.ResultVOUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BuyerService buyerService;

    //创建订单
    @PostMapping("/create")
    public ResultVO<Map<String,String>> create(@Valid OrderForm orderForm,
                                               BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.error("【创建订单】参数不正确,orderForm{}",orderForm);
            throw new SellException(ResultEnum.PARAM_ERROR,bindingResult.getFieldError().getDefaultMessage());
        }

        OrderDTO orderDTO = OrderForm2OrderDTOConverter.convert(orderForm);
        if(CollectionUtils.isEmpty(orderDTO.getOrderDetailList())){
            log.error("【创建订单】购物车不能为空,orderDTO={}",orderDTO);
            throw  new SellException(ResultEnum.CART_EMPTY);
        }
        OrderDTO result = orderService.create(orderDTO);
        Map<String,String> map = new HashMap<>();
        map.put("orderId",result.getOrderId());
        return ResultVOUtil.success(map);
    }

    //订单列表
    @GetMapping("/list")
    public ResultVO<List<OrderDTO>> list(@RequestParam("openid") String openid,
                                         @RequestParam(value = "page",defaultValue = "0") Integer page,
                                         @RequestParam(value = "size",defaultValue = "10") Integer size){

        if(StringUtils.isEmpty(openid)){
            log.error("【查询订单列表】openid不能为空,openid={}",openid);
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        PageRequest pageRequest = new PageRequest(page, size);
        Page<OrderDTO> orderDTOPage = orderService.findList(openid, pageRequest);
        return ResultVOUtil.success(orderDTOPage.getContent());

    }

    //订单详情
    @GetMapping("/detail")
    public ResultVO<OrderDTO> detail(@RequestParam("openid") String openid,
                                     @RequestParam("orderId") String orderId){
        if(org.apache.commons.lang3.StringUtils.isBlank(openid) || org.apache.commons.lang3.StringUtils.isBlank(orderId)){
            throw new SellException(ResultEnum.OPENID_OR_ORDERID_IS_NULL);
        }

        OrderDTO orderDTO = buyerService.findOrderOne(openid, orderId);
        return ResultVOUtil.success(orderDTO);
    }

    //取消订单
    @GetMapping("/cancel")
    public ResultVO cancel(@RequestParam("openid") String openid,
                                     @RequestParam("orderId") String orderId){
        if(org.apache.commons.lang3.StringUtils.isBlank(openid) || org.apache.commons.lang3.StringUtils.isBlank(orderId)){
            throw new SellException(ResultEnum.OPENID_OR_ORDERID_IS_NULL);
        }

       buyerService.cancelOrder(openid,orderId);
        return ResultVOUtil.success();
    }



}

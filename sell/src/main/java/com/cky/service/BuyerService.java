package com.cky.service;

import com.cky.dto.OrderDTO;

public interface BuyerService {

    OrderDTO findOrderOne(String openid,String orderId);

    OrderDTO cancelOrder(String openid,String orderId);
}

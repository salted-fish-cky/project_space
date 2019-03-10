package com.cky.service.impl;

import com.cky.dto.CartDTO;
import com.cky.enums.ProductStatusEnum;
import com.cky.enums.ResultEnum;
import com.cky.exception.SellException;
import com.cky.pojo.ProductInfo;
import com.cky.repository.ProductInfoRepository;
import com.cky.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ProductInfo findOne(String productId) {
        return productInfoRepository.findOne(productId);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ProductInfo> findUpAll() {
        return productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoRepository.findAll(pageable);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return productInfoRepository.save(productInfo);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void increaseStock(List<CartDTO> list) {
        for(CartDTO cartDTO : list){
            ProductInfo productInfo = findOne(cartDTO.getProductId());
            if(productInfo == null){
                new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer number = productInfo.getProductStock() + cartDTO.getProductQuantity();
//            if(number < 0){
//                new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
//            }
            productInfo.setProductStock(number);
            productInfoRepository.save(productInfo);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseStock(List<CartDTO> list) {
        for(CartDTO cartDTO : list){
            ProductInfo productInfo = findOne(cartDTO.getProductId());
            if(productInfo == null){
                new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer number = productInfo.getProductStock() - cartDTO.getProductQuantity();
            if(number < 0){
                new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(number);
            productInfoRepository.save(productInfo);
        }
    }
}

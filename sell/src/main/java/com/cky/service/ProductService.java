package com.cky.service;

import com.cky.dto.CartDTO;
import com.cky.pojo.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 商品
 */
public interface ProductService {

    ProductInfo findOne(String productId);

    /**
     * 查询上架商品
     * @return
     */
    List<ProductInfo> findUpAll();

    Page<ProductInfo> findAll(Pageable pageable);



    ProductInfo save(ProductInfo productInfo);

    //加库存
    void increaseStock(List<CartDTO> list);
    //减库存
    void decreaseStock(List<CartDTO> list);
}

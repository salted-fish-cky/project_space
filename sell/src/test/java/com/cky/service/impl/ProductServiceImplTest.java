package com.cky.service.impl;

import com.cky.enums.ProductStatusEnum;
import com.cky.pojo.ProductInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;
    @Test
    public void findOne() {
        ProductInfo one = productService.findOne("0812e83399914dc89f7e57e6f911e3cb");
        System.out.println(one);
    }

    @Test
    public void findUpAll() {
        List<ProductInfo> upAll = productService.findUpAll();
        System.out.println(upAll);
    }

    @Test
    public void findAll() {
    }

    @Test
    public void save() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setProductId(UUID.randomUUID().toString().replaceAll("-",""));
        productInfo.setProductName("女生最爱");
        productInfo.setCategoryType(2);
        productInfo.setProductDescription("111");
        productInfo.setProductIcon("http://xxxx.jpg");
        productInfo.setProductPrice(new BigDecimal(13.4));
        productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
        productInfo.setProductStock(100);
        productService.save(productInfo);
    }
}
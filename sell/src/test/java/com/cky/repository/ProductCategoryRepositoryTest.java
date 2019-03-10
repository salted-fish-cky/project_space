package com.cky.repository;

import com.cky.pojo.ProductCategory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryRepositoryTest {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Test
    public void findByCategoryTypeIn() {
        List<Integer> list = Arrays.asList(1);
        List<ProductCategory> byCategoryTypeIn = productCategoryRepository.findByCategoryTypeIn(list);
        System.out.println(byCategoryTypeIn);
    }

    @Test
    public void insert() {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("女生最爱");
        productCategory.setCategoryType(2);
        productCategoryRepository.save(productCategory);
    }
}
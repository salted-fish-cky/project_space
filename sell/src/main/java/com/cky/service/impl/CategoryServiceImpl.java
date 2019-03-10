package com.cky.service.impl;

import com.cky.pojo.ProductCategory;
import com.cky.repository.ProductCategoryRepository;
import com.cky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品类目
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ProductCategory findOne(Integer categoryId) {
        return productCategoryRepository.findOne(categoryId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ProductCategory> findAll() {
        return productCategoryRepository.findAll();
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        return productCategoryRepository.findByCategoryTypeIn(categoryTypeList);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public ProductCategory save(ProductCategory productCategory) {
        return productCategoryRepository.save(productCategory);
    }
}

package com.cky.jd.dao;

import java.util.List;

import com.cky.jd.beans.ProductModel;

public interface JdDao {

	public List<ProductModel> selectProductModelListByQuery(String queryString,String catalog_name,
			String price,String sort) throws Exception;
}

package com.cky.jd.service;

import java.util.List;

import com.cky.jd.beans.ProductModel;

public interface JdService {

	public List<ProductModel> selectProductModelListByQuery(String queryString,String catalog_name,
			String price,String sort) throws Exception;
}

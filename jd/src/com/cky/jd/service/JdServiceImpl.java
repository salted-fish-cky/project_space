package com.cky.jd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cky.jd.beans.ProductModel;
import com.cky.jd.dao.JdDao;

@Service
public class JdServiceImpl implements JdService{

	
	@Autowired
	private JdDao jdDao;
	
	public List<ProductModel> selectProductModelListByQuery(String queryString,String catalog_name,
			String price,String sort) throws Exception{
		
		return jdDao.selectProductModelListByQuery(queryString, catalog_name, price, sort);
		
	}
}

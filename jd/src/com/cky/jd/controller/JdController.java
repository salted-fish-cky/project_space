package com.cky.jd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cky.jd.beans.ProductModel;
import com.cky.jd.service.JdService;

/**
 * ��ѯ��Ʒ�б�
 * @author cky
 *
 */
@Controller
public class JdController {

	
	@Autowired
	private JdService jdService;
	
	//��Ʒ�б�
	@RequestMapping(value="list.action")
	public String list(String queryString,String catalog_name,
			String price,String sort,Model model) throws Exception{
		
		List<ProductModel> productModels = jdService.selectProductModelListByQuery(queryString, catalog_name, price, sort);
		
		model.addAttribute("productModels", productModels);
		model.addAttribute("queryString", queryString);
		model.addAttribute("catalog_name", catalog_name);
		model.addAttribute("price", price);
		model.addAttribute("sort", sort);

		return "product_list";
	}
}

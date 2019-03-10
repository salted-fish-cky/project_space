package com.cky.jd.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cky.jd.beans.ProductModel;

@Repository
public class JdDaoImpl  implements JdDao{
	
	@Autowired
	private SolrServer solrServer;
	
	//ͨ�������ĸ�������ѯ������Ʒ�����
	public List<ProductModel> selectProductModelListByQuery(String queryString,String catalog_name,
			String price,String sort) throws Exception{
		
		
		SolrQuery solrQuery = new SolrQuery();
		//�ؼ���
		solrQuery.setQuery(queryString);
		//��������
		if(null != catalog_name && !"".equals(catalog_name)){
			solrQuery.set("fq", "product_catalog_name"+catalog_name);
			
		}
		//�۸�����
		
		if(null != price && !"".equals(price)){
			String[] p = price.split("-");
			solrQuery.set("fq", "product_price:[" +p[0]+" TO " +p[1]+"]");
			
		} 	
		//�۸�����
		if("1".equals(sort)){
			solrQuery.addSort("product_price",ORDER.desc);
		}else{
			solrQuery.addSort("product_price",ORDER.asc);
		}
		
		//��ҳ
		solrQuery.setStart(0);
		solrQuery.setRows(16);
		//����Ĭ����
		solrQuery.set("df","product_keywords");
		//ֻ��ѯָ����
		solrQuery.set("fl", "id,product_name,product_price,product_picture");
		//����
		//�򿪿���
		solrQuery.setHighlight(true);
		//ָ���������ֶ�
		solrQuery.addHighlightField("product_name");
		//ǰ׺
		solrQuery.setHighlightSimplePre("<span style='color:red'>");
		//��׺
		solrQuery.setHighlightSimplePost("</span>");
		
		
		
		
		//ִ�в�ѯ
		QueryResponse response = solrServer.query(solrQuery);
		//�ĵ������
		SolrDocumentList docs = response.getResults();
		
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		//������
		long numFound = docs.getNumFound();
		
		List<ProductModel> productModels = new ArrayList<>();

		for (SolrDocument doc : docs) {
			String id = (String) doc.get("id");
			
			ProductModel productModel = new ProductModel();
			productModel.setPid(id);
			productModel.setPicture((String) doc.get("product_picture"));
			productModel.setPrice((float) doc.get("product_price"));
//			productModel.setName("product_name");
			
			//����
			Map<String, List<String>> map = highlighting.get(doc.get("id"));
			List<String> list = map.get("product_name");
			if(list != null){
				System.out.println(list.get(0));
				productModel.setName(list.get(0));
			}
//			
			
			productModels.add(productModel);
		}
		return productModels;
	}
}

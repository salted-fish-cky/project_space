package com.cky.sparkproject.dao.impl;


import com.cky.sparkproject.dao.ISessionDetailDAO;
import com.cky.sparkproject.domain.SessionDetail;
import com.cky.sparkproject.jdbc.JdbcHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * session明细DAO实现类
 * @author Administrator
 *
 */
public class SessionDetailDAOImpl implements ISessionDetailDAO {

	/**
	 * 插入一条session明细数据
	 * @param sessionDetail 
	 */
	public void insert(SessionDetail sessionDetail) {
		String sql = "insert into session_detail values(?,?,?,?,?,?,?,?,?,?,?,?)";  
		
		Object[] params = new Object[]{sessionDetail.getTaskid(),
				sessionDetail.getUserid(),
				sessionDetail.getSessionid(),
				sessionDetail.getPageid(),
				sessionDetail.getActionTime(),
				sessionDetail.getSearchKeyword(),
				sessionDetail.getClickCategoryId(),
				sessionDetail.getClickProductId(),
				sessionDetail.getOrderCategoryIds(),
				sessionDetail.getOrderProductIds(),
				sessionDetail.getPayCategoryIds(),
				sessionDetail.getPayProductIds()};  
		
		JdbcHelper jdbcHelper = JdbcHelper.getInstance();
		jdbcHelper.executeUpdate(sql, params);
	}

	@Override
	public void insert(List<SessionDetail> list) {
		String sql = "insert into session_detail values(?,?,?,?,?,?,?,?,?,?,?,?)";

		List<Object[]> paramList = new ArrayList<Object[]>();

		for (SessionDetail sessionDetail : list){

			Object[] params = new Object[]{sessionDetail.getTaskid(),
					sessionDetail.getUserid(),
					sessionDetail.getSessionid(),
					sessionDetail.getPageid(),
					sessionDetail.getActionTime(),
					sessionDetail.getSearchKeyword(),
					sessionDetail.getClickCategoryId(),
					sessionDetail.getClickProductId(),
					sessionDetail.getOrderCategoryIds(),
					sessionDetail.getOrderProductIds(),
					sessionDetail.getPayCategoryIds(),
					sessionDetail.getPayProductIds()};

			paramList.add(params);
		}

		JdbcHelper jdbcHelper = JdbcHelper.getInstance();
		jdbcHelper.executeBatch(sql,paramList);
	}

}

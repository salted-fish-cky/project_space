package com.cky.sparkproject.dao;


import com.cky.sparkproject.domain.SessionDetail;

import java.util.List;

/**
 * Session明细DAO接口
 * @author Administrator
 *
 */
public interface ISessionDetailDAO {

	/**
	 * 插入一条session明细数据
	 * @param sessionDetail 
	 */
	void insert(SessionDetail sessionDetail);

	/**
	 * 批量插入
	 */
	void insert(List<SessionDetail> list);
	
}

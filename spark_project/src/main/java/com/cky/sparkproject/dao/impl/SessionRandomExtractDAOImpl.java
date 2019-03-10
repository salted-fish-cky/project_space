package com.cky.sparkproject.dao.impl;


import com.cky.sparkproject.dao.ISessionRandomExtractDAO;
import com.cky.sparkproject.domain.SessionRandomExtract;
import com.cky.sparkproject.jdbc.JdbcHelper;

/**
 * 随机抽取session的DAO实现
 * @author Administrator
 *
 */
public class SessionRandomExtractDAOImpl implements ISessionRandomExtractDAO {

	/**
	 * 插入session随机抽取
	 * @param
	 */
	public void insert(SessionRandomExtract sessionRandomExtract) {
		String sql = "insert into session_random_extract values(?,?,?,?,?)";
		
		Object[] params = new Object[]{sessionRandomExtract.getTaskid(),
				sessionRandomExtract.getSessionid(),
				sessionRandomExtract.getStartTime(),
				sessionRandomExtract.getClickCategoryIds(),
				sessionRandomExtract.getSearchKeywords()
				};
		
		JdbcHelper jdbcHelper = JdbcHelper.getInstance();
		jdbcHelper.executeUpdate(sql, params);
	}
	
}

package com.cky.sparkproject.dao.impl;


import com.cky.sparkproject.dao.ITop10CategoryDAO;
import com.cky.sparkproject.domain.Top10Category;
import com.cky.sparkproject.jdbc.JdbcHelper;

/**
 * top10品类DAO实现
 * @author Administrator
 *
 */
public class Top10CategoryDAOImpl implements ITop10CategoryDAO {

	@Override
	public void insert(Top10Category category) {
		String sql = "insert into top10_category values(?,?,?,?,?)";  
		
		Object[] params = new Object[]{category.getTaskid(),
				category.getCategoryid(),
				category.getClickCount(),
				category.getOrderCount(),
				category.getPayCount()};  
		
		JdbcHelper jdbcHelper = JdbcHelper.getInstance();
		jdbcHelper.executeUpdate(sql, params);
	}

}

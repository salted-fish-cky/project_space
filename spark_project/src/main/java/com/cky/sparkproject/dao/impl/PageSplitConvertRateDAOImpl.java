package com.cky.sparkproject.dao.impl;


import com.cky.sparkproject.dao.IPageSplitConvertRateDAO;
import com.cky.sparkproject.domain.PageSplitConvertRate;
import com.cky.sparkproject.jdbc.JdbcHelper;

/**
 * 页面切片转化率DAO实现类
 * @author Administrator
 *
 */
public class PageSplitConvertRateDAOImpl implements IPageSplitConvertRateDAO {

	@Override
	public void insert(PageSplitConvertRate pageSplitConvertRate) {
		String sql = "insert into page_split_convert_rate values(?,?)";  
		Object[] params = new Object[]{pageSplitConvertRate.getTaskid(), 
				pageSplitConvertRate.getConvertRate()};
		
		JdbcHelper jdbcHelper = JdbcHelper.getInstance();
		jdbcHelper.executeUpdate(sql, params);
	}

}

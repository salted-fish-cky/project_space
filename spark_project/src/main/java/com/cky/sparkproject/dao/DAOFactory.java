package com.cky.sparkproject.dao;

import com.cky.sparkproject.dao.impl.*;

/**
 * DAO工厂类
 * @author Administrator
 *
 */
public class DAOFactory {

	/**
	 * 获取任务管理DAO
	 * @return DAO
	 */
	public static ITaskDAO getTaskDAO() {
		return new TaskDAOImpl();
	}

	public static ISessionAggrStatDAO getSessionAggrStatDAO(){
		return new SessionAggrStatDAOImpl();
	}

	public static ISessionRandomExtractDAO getSessionRandomExtractDAO(){

		return new SessionRandomExtractDAOImpl();
	}

	public static ISessionDetailDAO getSessionDetailDAO(){

		return new SessionDetailDAOImpl();
	}

	public static ITop10CategoryDAO getTop10CategoryDAO(){
		return new Top10CategoryDAOImpl();
	}

	public static ITop10SessionDAO getTop10SessionDAO(){
		return new Top10SessionDAOImpl();
	}

	public static IPageSplitConvertRateDAO getPageSplitConvertRateDAO(){
		return new PageSplitConvertRateDAOImpl();
	}

	public static IAreaTop3ProductDAO getAreaTop3ProductDAO(){
		return new AreaTop3ProductDAOImpl();
	}

	public static IAdUserClickCountDAO getAdUserClickCountDAO(){
		return new IAdUserClickCountDAOImpl();
	}

	public static IAdBlackListDao getAdBlackListDao(){
		return new IAdBlackListDaoImpl();
	}

	public static IAdStatDAO getAdStatDAO(){
		return new IAdStatDAOImpl();
	}

	public static IAdProvinceTop3DAO getAdProvinceTop3DAO(){
		return new AdProvinceTop3DAOImpl();
	}

	public static IAdClickTrendDAO getIAdClickTrendDAO(){
		return new IAdClickTrendDAOImpl();
	}

}

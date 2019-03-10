package com.cky.sparkproject.dao.impl;

import com.cky.sparkproject.dao.IAdClickTrendDAO;
import com.cky.sparkproject.domain.AdClickTrend;
import com.cky.sparkproject.jdbc.JdbcHelper;
import com.cky.sparkproject.model.AdClickTrendQueryResult;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class IAdClickTrendDAOImpl implements IAdClickTrendDAO {

    @Override
    public void updateBatch(List<AdClickTrend> adClickTrends) {
        JdbcHelper jdbcHelper = JdbcHelper.getInstance();

        List<AdClickTrend> insertAdClickTrends = new ArrayList<AdClickTrend>();
        List<AdClickTrend> updateAdClickTrends = new ArrayList<AdClickTrend>();

        String selectSQL = "select count(*) " +
                "from ad_click_trend " +
                "where date=? " +
                "and hour=? " +
                "and minute=? " +
                "and ad_id=?";


        for (AdClickTrend adClickTrend : adClickTrends){
            final AdClickTrendQueryResult queryResult = new AdClickTrendQueryResult();

            Object[] params = {adClickTrend.getDate(),
                    adClickTrend.getHour(),
                    adClickTrend.getMinute(),
                    adClickTrend.getAdId()};

            jdbcHelper.executeQuery(selectSQL, params, new JdbcHelper.QueryCallback() {
                @Override
                public void process(ResultSet rs) throws Exception {
                    if(rs.next()){
                        int count = rs.getInt(1);
                        queryResult.setCount(count);
                    }

                }
            });


            if(queryResult.getCount() >0){
                updateAdClickTrends.add(adClickTrend);
            }else {
                insertAdClickTrends.add(adClickTrend);
            }
        }

        //执行批量更新操作
        String updateSQL = "update ad_click_trend set click_count=? " +
                "where date=? " +
                "and hour=? " +
                "and minute=? " +
                "and ad_id=?";

        List<Object[]> updateParamsList = new ArrayList<Object[]>();

        for (AdClickTrend adClickTrend: adClickTrends){
            Object[] params = {adClickTrend.getClickCount(),
                    adClickTrend.getDate(),
                    adClickTrend.getHour(),
                    adClickTrend.getMinute(),
                    adClickTrend.getAdId()};
            updateParamsList.add(params);
        }

        jdbcHelper.executeBatch(updateSQL,updateParamsList);


        //执行批量插入操作
        String insertSQL = "insert into ad_click_trend values(?,?,?,?,?)";

        List<Object[]> insertParamsList = new ArrayList<Object[]>();

        for (AdClickTrend adClickTrend: adClickTrends){
            Object[] params = {
                    adClickTrend.getDate(),
                    adClickTrend.getHour(),
                    adClickTrend.getMinute(),
                    adClickTrend.getAdId(),
                    adClickTrend.getClickCount()};
            insertParamsList.add(params);
        }

        jdbcHelper.executeBatch(insertSQL,insertParamsList);



    }
}

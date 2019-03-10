package com.cky.sparkproject.dao.impl;

import com.cky.sparkproject.dao.IAdUserClickCountDAO;
import com.cky.sparkproject.domain.AdUserClickCount;
import com.cky.sparkproject.jdbc.JdbcHelper;
import com.cky.sparkproject.model.AdUserClickCountQueryResult;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class IAdUserClickCountDAOImpl implements IAdUserClickCountDAO {

    @Override
    public void updateBatch(List<AdUserClickCount> adUserClickCounts) {
        List<AdUserClickCount> insertAdUserClickCounts = new ArrayList<AdUserClickCount>();
        List<AdUserClickCount> updateAdUserClickCounts = new ArrayList<AdUserClickCount>();

        String selectSQL =
                "select count(*) from " +
                        "ad_user_click_count " +
                        "where date=? and user_id=? and ad_id=?";

        Object[] selectParams = null;

        JdbcHelper jdbcHelper = JdbcHelper.getInstance();

        for (AdUserClickCount adUserClickCount : adUserClickCounts){

            selectParams = new Object[]{adUserClickCount.getDate(),
            adUserClickCount.getUserId(),adUserClickCount.getAdId()};
            final AdUserClickCountQueryResult queryResult = new AdUserClickCountQueryResult();

            jdbcHelper.executeQuery(selectSQL, selectParams, new JdbcHelper.QueryCallback() {
                @Override
                public void process(ResultSet rs) throws Exception {
                    if(rs.next()){
                        int count = rs.getInt(1);
                        queryResult.setCount(count);
                    }
                }
            });

            int count = queryResult.getCount();
            if(count >0){
                updateAdUserClickCounts.add(adUserClickCount);
            }else {
                insertAdUserClickCounts.add(adUserClickCount);
            }

        }
        //执行批量插入
        String insertSQL = "insert into ad_user_click_count values(?,?,?,?)";

        List<Object[]> insertParamList = new ArrayList<Object[]>();

        for (AdUserClickCount adUserClickCount : insertAdUserClickCounts){
            Object[] insertParams = {adUserClickCount.getDate(),
                    adUserClickCount.getUserId(), adUserClickCount.getAdId(),
                    adUserClickCount.getClickCount()};

            insertParamList.add(insertParams);
        }

        jdbcHelper.executeBatch(insertSQL,insertParamList);

        //执行批量更新

        String updateSQL = "UPDATE ad_user_click_count SET click_count=click_count+? "
                + "WHERE date=? AND user_id=? AND ad_id=? ";

        List<Object[]> updateParamList = new ArrayList<Object[]>();

        for (AdUserClickCount adUserClickCount : updateAdUserClickCounts){
            Object[] updateParams = {adUserClickCount.getClickCount(),
                    adUserClickCount.getDate(),
                    adUserClickCount.getUserId(),
                    adUserClickCount.getAdId()};

            updateParamList.add(updateParams);
        }

        jdbcHelper.executeBatch(updateSQL,updateParamList);

    }

    @Override
    public int findByMultiKey(String date, long userId, long adId) {
        String sql = "select click_count " +
                "from ad_user_click_count " +
                "where date=? " +
                "and user_id=? " +
                "and ad_id=?";
        final AdUserClickCountQueryResult queryResult = new AdUserClickCountQueryResult();
        Object[] params = {date, userId, adId};
        JdbcHelper jdbcHelper = JdbcHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JdbcHelper.QueryCallback() {
            @Override
            public void process(ResultSet rs) throws Exception {
                if(rs.next()){
                    int clickCount = rs.getInt(1);
                    queryResult.setClickCount(clickCount);
                }
            }
        });
        int clickCount = queryResult.getClickCount();
        return clickCount;
    }
}

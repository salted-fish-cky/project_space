package com.cky.sparkproject.dao.impl;

import com.cky.sparkproject.dao.IAdStatDAO;
import com.cky.sparkproject.domain.AdStat;
import com.cky.sparkproject.jdbc.JdbcHelper;
import com.cky.sparkproject.model.AdStatQueryResult;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class IAdStatDAOImpl implements IAdStatDAO {
    @Override
    public void updateBatch(List<AdStat> adStats) {

        JdbcHelper jdbcHelper = JdbcHelper.getInstance();
        List<AdStat> insertAdStats = new ArrayList<AdStat>();
        List<AdStat> updateAdStats = new ArrayList<AdStat>();

        String selectSQL = "select count(*) " +
                "from ad_stat " +
                "where date=? " +
                "and province =? " +
                "and city=? " +
                "and ad_id=?";

        for (AdStat adStat : adStats){
            Object[] params = {adStat.getDate(),
                    adStat.getProvice(),
                    adStat.getCity(),
                    adStat.getAdId()};

            final AdStatQueryResult queryResult = new AdStatQueryResult();
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
                updateAdStats.add(adStat);
            }else {
                insertAdStats.add(adStat);
            }
        }

        String insertSQL = "insert into ad_stat values(?,?,?,?,?)";

        List<Object[]> insertParamsList = new ArrayList<Object[]>();

        for (AdStat adStat : insertAdStats){
            Object[] params = new Object[]{adStat.getDate(),
            adStat.getProvice(),
            adStat.getCity(),
            adStat.getAdId(),
            adStat.getClickCount()};
            insertParamsList.add(params);
        }

        jdbcHelper.executeBatch(insertSQL,insertParamsList);

        //对于要更新的数据，执行批量更新
        String updateSQL = "UPDATE ad_stat SET click_count=? "
                + "WHERE date=? "
                + "AND province=? "
                + "AND city=? "
                + "AND ad_id=?";
        List<Object[]> updateParamsList = new ArrayList<Object[]>();

        for (AdStat adStat : updateAdStats){
            Object[] params = new Object[]{ adStat.getClickCount(),
                    adStat.getDate(),
                    adStat.getProvice(),
                    adStat.getCity(),
                    adStat.getAdId()};
            updateParamsList.add(params);
        }

        jdbcHelper.executeBatch(updateSQL,updateParamsList);


    }

}

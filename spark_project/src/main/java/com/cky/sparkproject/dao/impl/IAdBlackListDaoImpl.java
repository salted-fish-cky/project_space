package com.cky.sparkproject.dao.impl;

import com.cky.sparkproject.dao.IAdBlackListDao;
import com.cky.sparkproject.domain.AdBlacklist;
import com.cky.sparkproject.jdbc.JdbcHelper;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class IAdBlackListDaoImpl implements IAdBlackListDao {
    @Override
    public void insertBatch(List<AdBlacklist> adBlacklists) {
        String sql = "insert into ad_blacklist values(?)";
        List<Object[]> paramsList = new ArrayList<Object[]>();

        for (AdBlacklist adBlacklist: adBlacklists){
            Object[] params = {adBlacklist.getUserId()};
            paramsList.add(params);
        }

        JdbcHelper jdbcHelper = JdbcHelper.getInstance();

        jdbcHelper.executeBatch(sql,paramsList);

    }

    @Override
    public List<AdBlacklist> findAll() {

        String sql = "select * from ad_blacklist";
        final List<AdBlacklist> adBlacklists =  new ArrayList<AdBlacklist>();

        JdbcHelper jdbcHelper = JdbcHelper.getInstance();
        jdbcHelper.executeQuery(sql, null, new JdbcHelper.QueryCallback() {
            @Override
            public void process(ResultSet rs) throws Exception {
                while (rs.next()){
                    long userId = rs.getInt(1);
                    AdBlacklist adBlacklist = new AdBlacklist();
                    adBlacklist.setUserId(userId);
                    adBlacklists.add(adBlacklist);
                }
            }
        });
        return adBlacklists;
    }
}

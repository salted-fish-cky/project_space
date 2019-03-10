package com.cky.sshzz.dao.impl;


import com.cky.sshzz.dao.CustomerDao;
import com.cky.sshzz.entities.Customer;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateCallback;

import java.util.List;

public class CustomerDaoImpl extends BaseDaoImpl<Customer> implements CustomerDao {

    @Override
    public List<Object[]> getIndustryCount() {
        //原生sql查询
        List<Object[]> list = getHibernateTemplate().execute(new HibernateCallback<List<Object[]>>() {
            @Override
            public List<Object[]> doInHibernate(Session session) throws HibernateException {
                String sql = "select bd.dict_item_name,count(*) total " +
                        "from customer c,base_dict bd where " +
                        "c.cust_industry=bd.dict_id " +
                        "group by c.cust_industry";
                Query query = session.createSQLQuery(sql);
                return query.list() ;
            }
        });
        return list;
    }
}

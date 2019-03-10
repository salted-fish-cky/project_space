package com.cky.hibernate.hql.test;


import com.cky.hibernate.hql.entities.Department;
import com.cky.hibernate.hql.entities.Employee;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.jdbc.Work;
import org.hibernate.query.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class HibernateTest {

    private SessionFactory sessionFactory;

    private Session session;

    private Transaction transaction;

    @Before
    public void init(){
        System.out.println("init");
        Configuration configure = new Configuration().configure();
        sessionFactory = configure.buildSessionFactory();
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    /**
     * 多表查询： select * from A,B
     *
     * 内连接：
     *      —隐式内连接：select * from A,B where B.aid = A.id
     *      -显式内连接：select * from A inner join B on b.aid = a.id
     *
     * 外连接：
     *      -左外：select * from A left join B on b.aid = id
     *      -右外：select * from A right join B on b.aid = id
     *
     *
     */

    @Test
    public void testHql(){
        //1.创建Query对象
        String hql = "from Employee e where e.salary < ? and e.phone like ? order by e.salary";
        Query query = session.createQuery(hql);
        //2.绑定参数
        query.setFloat(0,10000).setString(1,"%142%");

        //3.执行查询
        List<Employee> emps = query.list();
        System.out.println(emps.size());
    }

    /**
     * 分页查询
     * setFirstResult() 设定从哪开始
     * setNaxResults() 设定一次最多检索的数目
     */
    @Test
    public void testPageQuery(){
        String hql = "FROM Employee";
        Query query = session.createQuery(hql);
        int pageNo = 7;
        int pageSize = 3;
        List list = query.setFirstResult((pageNo - 1) * pageSize).setMaxResults(pageSize).list();
        System.out.println(list);
    }

    /**
     * 投影查询：查询结果仅包含实体的部分属性，通过select关键字实现
     */
    @Test
    public void testFieldQuery(){
        String hql = "select e.salary,e.phone,e.dept from Employee e where e.dept=?";
        Query query = session.createQuery(hql);
        Department dept = new Department();
        dept.setId(6);
        List<Object[]> result = query.setParameter(0,dept).list();
        System.out.println(result);
        for(Object[] objects:result){
            System.out.println(objects);
        }
    }

    @Test
    public void testFieldQuery2(){//返回对象集合
        String hql = "select new Employee(e.salary,e.phone,e.dept) from Employee e where e.dept=?";
        Query query = session.createQuery(hql);
        Department dept = new Department();
        dept.setId(6);
        List<Employee> result = query.setParameter(0,dept).list();
        System.out.println(result);
        for(Employee employee:result){
            System.out.println(employee.getId()+employee.getSalary()+employee.getPhone()+employee.getDept());
        }
    }

    @Test
    public void testGroupBy(){
        String hql = "select min(e.salary),max(e.salary) from " +
                "Employee e GROUP BY e.dept having min(salary)>?";
        Query query = session.createQuery(hql);
        List<Object[]> result = query.setFloat(0,4000).list();
        System.out.println(result);
        for(Object[] objects:result){
            System.out.println(Arrays.asList(objects));
        }


    }

    /**
     * 破外左式连接
     */
    @Test
    public void testLeftJoinFetch(){
        String hql = "FROM Department d left join fetch d.employees";
        Query query = session.createQuery(hql);
        List<Department> depts = query.list();
        System.out.println(depts.size());
        for(Department department:depts){
            System.out.println(department.getName()+department.getEmployees().size());
        }
    }

    /**
     * QBC查询
     */
    @Test
    public void tesQBC(){
        //1.创建一个Criteria对象
        Criteria criteria = session.createCriteria(Employee.class);
        //2.添加查询条件： 在QBC中查询条件使用Criteria来表示
        criteria.add(Restrictions.eq("name","bb"));

        //3.执行查询
        Employee employee = (Employee) criteria.uniqueResult();
        System.out.println(employee.getId());
    }

    @After
    public void destory(){
        System.out.println("destory");
        transaction.commit();
        session.close();
        sessionFactory.close();
    }

    @Test
    public void testDowork(){
        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                System.out.println(connection);
                //调用存储过程
            }
        });
    }

}

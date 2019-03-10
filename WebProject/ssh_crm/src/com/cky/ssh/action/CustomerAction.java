package com.cky.ssh.action;

import com.cky.ssh.entities.Customer;
import com.cky.ssh.service.CustomerService;
import com.cky.ssh.utils.PageBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class CustomerAction extends ActionSupport implements ModelDriven<Customer> {
    private Customer customer = new Customer();

    private CustomerService cs;


    private Integer currentPage;

    private Integer pageSize;

    public String list(){

        //封装离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(Customer.class);
        //判断并封装参数
        if(StringUtils.isNotBlank(customer.getCustName())){
            dc.add(Restrictions.like("name","%"+customer.getCustName()+"%"));
        }

        //1.调用service查询分页数据
        PageBean pb = cs.getPageBean(dc,currentPage,pageSize);
        //2.将pageBean放入到request域中，转发到列表页面显示
        ActionContext.getContext().put("pageBean",pb);
        return "list";
    }


    @Override
    public Customer getModel() {
        return customer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CustomerService getSc() {
        return cs;
    }

    public void setSc(CustomerService sc) {
        this.cs = sc;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}

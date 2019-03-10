package com.cky.sshzz.action;


import com.cky.sshzz.entities.Customer;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.cky.sshzz.service.CustomerService;
import com.cky.sshzz.utils.PageBean;

import java.io.File;
import java.util.List;

public class CustomerAction extends ActionSupport implements ModelDriven<Customer> {
    private Customer customer = new Customer();

    private CustomerService cs;


    private Integer currentPage;

    private Integer pageSize;

    private File photo; //上传的文件会自动保存封装到File对像中

    private String photoFileName; //上传文件的文件名

    private String photoContentType; //上传文件的类型




    public String list(){

        //封装离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(Customer.class);
        //判断并封装参数
        if(StringUtils.isNotBlank(customer.getCustName())){
            dc.add(Restrictions.like("custName","%"+customer.getCustName()+"%"));
        }

        //1.调用service查询分页数据
        PageBean pb = cs.getPageBean(dc,currentPage,pageSize);
        //2.将pageBean放入到request域中，转发到列表页面显示
        ActionContext.getContext().put("pageBean",pb);

        return "list";
    }


    public String add(){

        if(photo!= null){
            //将上传文件保存到指定路径
            photo.renameTo(new File("G:\\upload\\"+photoFileName));
            System.out.println("文件名："+photoFileName);

        }
        //1.调用service保存Customer对象
        cs.save(customer);
        return "toList";
    }

    public String toEdit(){
        //1.调用service根据id获得对象
        Customer c = cs.getById(customer.getCustId());
        //2.将客户放到request域中，并转发到编辑页面
        ActionContext.getContext().put("customer",c);
        return "edit";
    }

    public String industry(){

        List<Object[]> cout = cs.getIndustryCount();
        ActionContext.getContext().put("list",cout);
        return "industryCount";
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


    public File getPhoto() {
        return photo;
    }

    public void setPhoto(File photo) {
        this.photo = photo;

    }

    public String getPhotoFileName() {
        return photoFileName;
    }

    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }
}

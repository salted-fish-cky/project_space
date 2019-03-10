package com.cky.sshzz.action;

import com.cky.sshzz.entities.Customer;
import com.cky.sshzz.entities.SaleVisit;
import com.cky.sshzz.entities.User;
import com.cky.sshzz.service.SaleVisitService;
import com.cky.sshzz.utils.PageBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class SaleVisitAction extends ActionSupport implements ModelDriven<SaleVisit> {

    private SaleVisit saleVisit = new SaleVisit();
    private SaleVisitService saleVisitService;

    private Integer currentPage;

    private Integer pageSize;

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

    public void setSaleVisitService(SaleVisitService saleVisitService) {
        this.saleVisitService = saleVisitService;
    }


    public String list(){

        //封装离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(SaleVisit.class);
        //判断并封装参数
        if(saleVisit.getCustomer()!=null&&saleVisit.getCustomer().getCustId()!=null){
            System.out.println(saleVisit.getCustomer().getCustId());
            dc.add(Restrictions.eq("customer.custId",saleVisit.getCustomer().getCustId()));
        }

        //1.调用service查询分页数据
        PageBean pb = saleVisitService.getPageBean(dc,currentPage,pageSize);
        //2.将pageBean放入到request域中，转发到列表页面显示
        ActionContext.getContext().put("pageBean",pb);

        return "list";
    }

    public String add(){


        User user = (User) ActionContext.getContext().getSession().get("user");
        saleVisit.setUser(user);
        System.out.println(saleVisit);
        //调用service保存客户拜访记录
        saleVisitService.save(saleVisit);

        //重定向到客户拜访记录列表Action
        return "toList";
    }

    public String toEdit(){
        SaleVisit sv = saleVisitService.getById(saleVisit.getVisitId());
        //将对象放入到request域中
        ActionContext.getContext().put("saleVisit",sv);

        return "add";
    }
    @Override
    public SaleVisit getModel() {
        return saleVisit;
    }
}

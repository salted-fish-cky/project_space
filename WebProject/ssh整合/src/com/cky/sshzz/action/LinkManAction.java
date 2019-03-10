package com.cky.sshzz.action;

import com.cky.sshzz.entities.Customer;
import com.cky.sshzz.entities.LinkMan;
import com.cky.sshzz.service.LinkManService;
import com.cky.sshzz.utils.PageBean;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class LinkManAction extends ActionSupport implements ModelDriven<LinkMan>{

    private LinkMan linkMan = new LinkMan();

    private LinkManService linkManService;

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

    public void setLinkManService(LinkManService linkManService) {
        this.linkManService = linkManService;
    }

    public String add() throws Exception {
        //1.调用Service
        linkManService.save(linkMan);
        //2.重定向到联系人页面

        return "toList";
    }

    public String list(){

        //封装离线查询对象
        DetachedCriteria dc = DetachedCriteria.forClass(LinkMan.class);
        //判断并封装参数
        if(StringUtils.isNotBlank(linkMan.getLkmName())){
            dc.add(Restrictions.like("lkmName","%"+linkMan.getLkmName()+"%"));
        }

        if(linkMan.getCustomer()!=null){
            if(linkMan.getCustomer().getCustId()!=null)
            dc.add(Restrictions.eq("customer.custId",linkMan.getCustomer().getCustId()));
        }

        //1.调用service查询分页数据
        PageBean pb = linkManService.getPageBean(dc,currentPage,pageSize);
        //2.将pageBean放入到request域中，转发到列表页面显示
        ActionContext.getContext().put("pageBean",pb);

        return "list";
    }

    public String toEdit(){

        //调用service
        System.out.println(linkMan.getLkmId());
        LinkMan lm = linkManService.getById(linkMan.getLkmId());
        ActionContext.getContext().put("linkMan",lm);


        return "add";
    }

    @Override
    public LinkMan getModel() {
        return linkMan;
    }
}

package com.cky.bos.action;

import com.cky.bos.action.base.BaseAction;
import com.cky.bos.domain.Decidedzone;
import com.cky.bos.service.DecidedzoneService;
import crm.Customer;
import crm.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * 定区管理
 */
@Controller
@Scope("prototype")
public class DecidedzoneAction extends BaseAction<Decidedzone> {

    @Autowired
    private CustomerService proxy;

    private String[] subareaid;

    private List<Integer> customerIds;

    public void setCustomerIds(List<Integer> customerIds) {
        this.customerIds = customerIds;
    }

    public void setSubareaid(String[] subareaid) {
        this.subareaid = subareaid;
    }
    @Autowired
    private DecidedzoneService decidedzoneService;

    public String add(){
        decidedzoneService.save(subareaid,model);
        return LIST;
    }

    public String pageQuery(){

        decidedzoneService.pageQuery(pageBean);
        java2Json(pageBean,new String[] {"currentPage","detachedCriteria","pageSize","subarea","decidedzones"});
        return NONE;
    }

    /**
     * 远程调用crm服务，获取未关联定区的客户
     * @return
     */
    public String findListNotAssociation(){
        List<Customer> listNotAssociation = proxy.findListNotAssociation();
        java2Json(listNotAssociation,new String[]{});
        return NONE;
    }

    /**
     * 远程调用crm服务，获取已关联定区的客户
     * @return
     */
    public String findListHasAssociation(){

        String id = model.getId();
        List<Customer> list = proxy.findListHasAssociation(id);
        java2Json(list,new String[]{});
        return NONE;
    }



    /**
     * 远程调用crm服务，将客户关联到定区
     * @return
     */
    public String assigncustomerstodecidedzone(){

        proxy.assigncustomerstodecidedzone(model.getId(),customerIds);
        return LIST;
    }
}

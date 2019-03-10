package com.cky.bos.action;

import com.cky.bos.action.base.BaseAction;
import com.cky.bos.domain.Noticebill;
import com.cky.bos.service.NoticebillService;
import crm.Customer;
import crm.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
public class NoticebillAction extends BaseAction<Noticebill> {
    //注入crm端代理对象
    @Autowired
    private CustomerService proxy;
    @Autowired
    private NoticebillService noticebillService;

    /**
     * 远程调用crm服务，根据手机号查询客户信息
     * @return
     */
    public String findCustomerByTelephone(){
        String telephone = model.getTelephone();
        Customer customer = proxy.findCustomerByTelephone(telephone);
        java2Json(customer,new String[]{});
        return NONE;
    }

    /**
     * 保存一个通知单，并尝试自动分单
     * @return
     */
    public String add(){

        noticebillService.save(model);
        return "noticebill_add";
    }
}

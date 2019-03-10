package com.cky.bos.service.impl;

import com.cky.bos.dao.DecidedzoneDao;
import com.cky.bos.dao.NoticebillDao;
import com.cky.bos.dao.WorkbillDao;
import com.cky.bos.domain.*;
import com.cky.bos.service.NoticebillService;
import com.cky.bos.utils.BOSUtils;
import crm.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@Transactional
public class NoticebillServiceImpl implements NoticebillService {
    @Autowired
    private NoticebillDao noticebillDao;
    @Autowired
    private CustomerService proxy;
    @Autowired
    private DecidedzoneDao decidedzoneDao;
    @Autowired
    private WorkbillDao workbillDao;


    /**
     * 保存业务通知单并尝试自动分单
     * @param model
     */
    public void save(Noticebill model) {
        User user = BOSUtils.getLoginUser();
        model.setUser(user);//设置当前登录用户
        noticebillDao.save(model);
        //获取用户的取件地址
        String pickaddress = model.getPickaddress();
        //远程调用crm服务查询定区id
        String decidedzoneId = proxy.findDecidedzoneIdByAddress(pickaddress);
        if(decidedzoneId!=null){
            //查询到了定区id，能够自动分单
            Decidedzone decidedzone = decidedzoneDao.findById(decidedzoneId);
            Staff staff = decidedzone.getStaff();
            model.setStaff(staff);   //业务通知单关联取派员对象
            //设置分单类型为自动分单
            model.setOrdertype(Noticebill.ORDERTYPE_AUTO);

            //为取派员产生一个工单
            Workbill workbill = new Workbill();
            workbill.setAttachbilltimes(0);//追单次数
            workbill.setBuildtime(new Timestamp(System.currentTimeMillis()));//创建时间，当前系统时间
            workbill.setNoticebill(model);//工单关联业务通知单
            workbill.setPickstate(Workbill.PICKSTATE_NO);//取件状态
            workbill.setRemark(model.getRemark()); //备注信息
            workbill.setStaff(staff);    //工单关联取派员
            workbill.setType(Workbill.TYPE_1);//工单类型
            workbillDao.save(workbill);

            //调用短信平台，发送短信
        }else{
            //没有查询到定区id，不能够自动分单
            model.setOrdertype(Noticebill.ORDERTYPE_MAN);
        }


    }
}

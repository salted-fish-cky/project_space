package com.cky.bos.action;

import com.cky.bos.action.base.BaseAction;
import com.cky.bos.domain.Staff;
import com.cky.bos.service.StaffService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@Scope("prototype")
public class StaffAction extends BaseAction<Staff> {

    @Autowired
    private StaffService staffService;

    private List<String> ids;

    public void setIds(String ids) {
        this.ids = Arrays.asList(ids.split(","));
    }

    public String add(){
        staffService.add(model);
        return LIST;
    }

    /**
     * 分页查询方法
     *
     */
    public String pageQuery() throws IOException {

        staffService.pageQuery(pageBean);
        java2Json(pageBean,new String[]{"currentPage","detachedCriteria","pageSize","decidedzones"});

//        Map map = new HashMap();
//        map.put("total",pageBean.getTotal());
//        map.put("rows",pageBean.getRows());
//        String json = JSON.toJSONString(map);
//        ServletActionContext.getResponse().setContentType("text/json;charset=utf-8");
//        ServletActionContext.getResponse().getWriter().write(json);
        return NONE;
    }

    /**
     * 查询所有未删除的取派员返回json数据
     * @return
     */
    public String listAjax(){
        List<Staff> list =  staffService.findListNotDelete();
        java2Json(list,new String[] {"decidedzones"});
        return NONE;
    }

    @RequiresPermissions("staff-delete") //执行这个方法需要当前用户具有staff-delete权限
    public String deleteBatch(){
        staffService.deleteBatch(ids);
        return LIST;
    }

}

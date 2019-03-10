package com.cky.springmvc.crud.handler;

import com.cky.springmvc.crud.dao.DepartmentDao;
import com.cky.springmvc.crud.dao.EmployeeDao;
import com.cky.springmvc.crud.entities.Department;
import com.cky.springmvc.crud.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.rmi.MarshalledObject;
import java.util.List;
import java.util.Map;

@Controller
public class EmployeeHandler {

    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DepartmentDao departmentDao;




    @RequestMapping(value = "/emp",method = RequestMethod.PUT)
    public String update(Employee employee){

        employeeDao.save(employee);
        return "redirect:/emps";
    }

    @ModelAttribute
    public void getEmployee(@RequestParam(value = "id",required = false) Integer id,Map<String,Object> map){
        if(id!=null){
            map.put("employee",employeeDao.get(id));
        }

    }

    @RequestMapping(value = "/emp/{id}",method = RequestMethod.GET)
    public String input(@PathVariable("id") Integer id,Map<String,Object> map){
        map.put("employee",employeeDao.get(id));
        map.put("departments",departmentDao.getDepartments());
        return "input";
    }


    @RequestMapping(value = "/emp/{id}",method = RequestMethod.DELETE)
    public String delete(@PathVariable("id") Integer id){
        employeeDao.delete(id);
        return "redirect:/emps";
    }

    @RequestMapping(value = "emp",method = RequestMethod.POST)
    public String save(@Valid Employee employee, BindingResult result){
        System.out.println(result.getErrorCount());
        if(result.getErrorCount()>0){
            System.out.println("出错了！");
            for(FieldError error:result.getFieldErrors()){
                System.out.println(error.getField()+":"+error.getDefaultMessage());
            }

            //若验证出错则转向定制的页面
        }

        employeeDao.save(employee);

        return "redirect:/emps";
    }


    @RequestMapping(value = "emp",method = RequestMethod.GET)
    public String input(Map<String,Object> map){

        System.out.println(departmentDao.getDepartments());
        map.put("departments",departmentDao.getDepartments());

        map.put("employee",new Employee());

    return "input";
    }

    @RequestMapping("/emps")
    public String list(Map<String,Object> map){
        map.put("employees",employeeDao.getAll());
        return "list";
    }

//    @InitBinder
//    public void initBinder(WebDataBinder binder){
//        binder.setDisallowedFields("name");
//    }

}

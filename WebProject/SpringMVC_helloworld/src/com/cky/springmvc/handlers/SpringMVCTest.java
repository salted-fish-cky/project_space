package com.cky.springmvc.handlers;

import com.cky.springmvc.entities.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@SessionAttributes({"user"})
@Controller
public class SpringMVCTest {

    private static final String SUCCESS = "success";



    @RequestMapping("/testRedirect")
    public String testRedirect(){

        System.out.println("testRedirect");
        return "redirect:/index.jsp";
    }

    /**
     * 自定义试图
     * @return
     */
    @RequestMapping("/testView")
    public String testView(){
        System.out.println("testView");
        return "helloView";
    }



    /**
     * 有@ModelAttribute标记的方法，会在每个目标方法执行之前被SpringMVC调用
     * @param id
     * @param map
     */
    @ModelAttribute
    public void getUser(@RequestParam(value = "id",required = false) Integer id,Map<String,Object> map){
        if(id!=null){
            //模拟从数据库中获取对象
            User user = new User(1, "Tom", "123456", "Tom@qq.com", "12");
            System.out.println("从数据库中获取一个对象："+user);
            map.put("user",user);
        }
    }

    /**
     * ModelAttribute运行流程
     * 1.执行@ModelAttribute注解修饰的方法：从数据库中取出对象，把对像放到Map中，
     * 键为user
     * 2.SpringMVC从Map中取出User对象，并把表单的请求参数赋给该User对象的对应属性。
     * 3.SpringMVC把上述对象传入目标方法的参数
     *
     *
     * @param user
     * @return
     */
    @RequestMapping("/testModelAttribute")
    public String testModelAttribute(User user){
        System.out.println("修改"+user);
        return SUCCESS;
    }

    /**
     * @SessionAttributes 除了可以通过属性名指定需要放到会话中的属性外，
     * 还可以通过模型属性的对象类型指定哪些模型属性需要放到会话中
     *
     * 注意：该注解只能放在类的上面，而不能修饰方法
     * @param map
     * @return
     */
    @RequestMapping("/testSessionAttributes")
    public String testSessionAttributes(Map<String,Object> map){

        User user = new User("Tom", "12345", "tom@qq.com", "13");
         map.put("user",user);
        return SUCCESS;
    }

    /**
     * 目标方法可以添加Map类型(实际上也可以是Model类型和ModelMap类型)的参数
     * @param map
     * @return
     */
    @RequestMapping("/testMap")
    public String testMap(Map<String,Object> map){
        map.put("name", Arrays.asList("tom","cky","zz"));
        System.out.println(map.getClass().getName());
        return SUCCESS;
    }

    /**
     * 目标方法的返回值可以是ModelAndView类型
     * 其中可以视图和模型信息
     * SpringMVC会把ModelAndView中数据放入到request域对象中
     *
     * @return
     */
    @RequestMapping("/testModelAndView")
    public ModelAndView testModelAndView(){
        String viewName = SUCCESS;
        ModelAndView modelAndView = new ModelAndView(viewName);
        //添加模型数据到modelAndView中
        modelAndView.addObject("time",new Date());
        return modelAndView;
    }

    /**
     * 可以使用Servlet原生的API作为目标方法的参数
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/testServletAPI")
    public String testServletAPI(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("testServletAPI"+request+","+response);
        return SUCCESS;
    }

    /**
     * POJO
     * SpringMVC会按请求参数名和POJO属性名进行匹配，
     * 自动为该属性填充属性值，支持级联属性
     * @param user
     * @return
     */
    @RequestMapping("/testPojo")
    public String testPojo(User user){
        System.out.println("testPojo:"+user);
        return SUCCESS;
    }

    /**
     * @RequestParam 来映射请求参数,
     * value 值即为请求参数的参数名
     * required 该参数是否为必须，默认值为true
     * defaultValue 请求参数的默认值
     * @param username
     * @param age
     * @return
     */
    @RequestMapping("/testRequestParams")
    public String testRequestParams(@RequestParam(value = "username") String username,@RequestParam(value = "age",required = false) Integer age){
        System.out.println("testRequestParams,username"+username+",age:"+age);
        return SUCCESS;
    }

    /**
     * Rest风格的url
     * 以CURD为例：
     * 新增：/oder POST
     * 修改：/oder/1 PUT
     * 获取：/oder/1 GET
     * 删除：/oder/1 DELETE
     *
     * 如何发送PUT请求和DELETEq请求？
     * 1.需要配置HiddenHttpMethodFilter
     * 2.需要发送POST请求
     * 3.需要在发送POST请求时携带一个name="_method"的隐藏域，值为DElETE或PUT
     * @param id
      * @return
     */
    @RequestMapping(value = "/testRest/{id}",method = RequestMethod.GET)
    @ResponseBody()
    public String testRest(@PathVariable Integer id){
        System.out.println("testRest Get:"+id);

        return SUCCESS;
    }

    @RequestMapping(value = "/testRest",method = RequestMethod.POST)
    @ResponseBody()
    public String testRest(){
        System.out.println("testRest Post:");

        return SUCCESS;
    }

    @RequestMapping(value = "/testRest/{id}",method = RequestMethod.DELETE)
    @ResponseBody
    public String testRestDelete(@PathVariable Integer id){
        System.out.println("testRest Delete:"+id);

        return SUCCESS;
    }

    @RequestMapping(value = "/testRest/{id}",method = RequestMethod.PUT)
    @ResponseBody
    public String testRestPut(@PathVariable Integer id){
        System.out.println("testRest Put:"+id);

        return SUCCESS;
    }



    /**
     * 1.使用@RequestMapping注解来映射请求的URL
     * 2.返回值会通过视图解析器解析为实际的物理视图，对于InternalResourceViewResolver视图解析器，
     * 会做如下的解析 : prefix+returnVal+后缀
     * /WEB-INF/views/success.jsp
     * @return
     */
    @RequestMapping(value = "/hello")
    public String hello(){
        System.out.println("hello world");
        return SUCCESS;
    }


    @RequestMapping(value = "testParamsAndHeader",params = {"username","age!=10"})
    public String testParamsAndHeader(){
        System.out.println("testParamsAndHeader");
        return SUCCESS;
    }

    /**
     * url 支持通配符
     * @return
     */
    @RequestMapping("/testAntPath/*")
    public String testAntPath(){
        System.out.println("testAntPath");

        return SUCCESS;

    }

    /**
     * @PathVariable 可以映射URL中的占位符到目标方法的参数中
     * @param id
     * @return
     */
    @RequestMapping("testPathVariable/{id}")
    public String testPathVariable(@PathVariable("id") Integer id){

        System.out.println("testPathVariable："+id);
        return SUCCESS;
    }

}

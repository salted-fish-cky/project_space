package com.cky.springmvc.crud.test;

import com.cky.springmvc.crud.dao.DepartmentDao;
import com.cky.springmvc.crud.dao.EmployeeDao;
import com.cky.springmvc.crud.entities.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Controller
public class SpringMVCTest {
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private DepartmentDao departmentDao;

    @RequestMapping("/testFileUpload")
    public String testFileUpload(@RequestParam("file")MultipartFile file,@RequestParam("desc") String desc) throws IOException {

        System.out.println("desc:"+desc);
        System.out.println("OriginalFile"+file.getOriginalFilename());
        System.out.println("InputStream"+file.getInputStream());

        return "success";
    }


    @RequestMapping("/testResponseEntity")
    public ResponseEntity<byte[]> testResponseEntity(HttpSession session ) throws IOException {
        byte[] body = null;
        ServletContext servletContext = session.getServletContext();
        InputStream in = servletContext.getResourceAsStream("/file/android studio.desktop");
        body = new byte[in.available()];
        in.read(body);
        HttpHeaders headers =  new HttpHeaders();
        headers.add("Content-Disposition","attachment;");

        HttpStatus status = HttpStatus.OK;
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(body,headers,status);

        return response;
    }


    @ResponseBody
    @RequestMapping("/testHttpMessageConverter")
    public String testHttpMessageConverter(@RequestBody String body){
        System.out.println(body);

        return "helloworld!"+new Date();
    }


    @RequestMapping("/testJson")
    @ResponseBody
    public Employee testJson(){
        return employeeDao.get(1001);
    }
}

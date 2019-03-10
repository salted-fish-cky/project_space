package com.cky.upload.servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

@WebServlet(name = "DownloadServlet",urlPatterns = "/download")
public class DownloadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String filename = request.getParameter("filename");
        String filepath = request.getParameter("filepath");
//        String filepath = "E:\\idea_workspace\\Web\\out\\artifacts\\_war_exploded2\\WEB-INF\\files\\15087615433077830.jpg";
        //设置contentType响应头：设置响应头类型，类型为下载类型
        response.setContentType("aplication/x-msdownload");
        //设置Content-Disposition响应头：通知浏览器不再由浏览器自行处理要下载的文件，而是由用户手工完成
        response.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(filename,"utf-8"));

        ServletOutputStream out = response.getOutputStream();
        System.out.println(filename+"---"+filepath);
        InputStream in = new FileInputStream(filepath);
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = in.read(buffer))!=-1){
            out.write(buffer,0,len);
        }
        in.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);

    }
}

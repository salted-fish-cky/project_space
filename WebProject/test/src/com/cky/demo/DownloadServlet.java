package com.cky.demo;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;

@WebServlet(name = "DownloadServlet",urlPatterns = "/download")
public class DownloadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        //将服务器的资源返回到浏览器
        String fileName = request.getParameter("fileName");

        //通知浏览器是下载，而不是预览
        String mimeType = this.getServletContext().getMimeType(fileName);
        response.setContentType(mimeType);
        response.setHeader("Content-Disposition","attachment;fileName="+fileName);
        //获取文件的绝对路径
        String realPath = this.getServletContext().getRealPath("download/" + fileName);
        FileInputStream fileInputStream = new FileInputStream(realPath);
        ServletOutputStream outputStream = response.getOutputStream();
        int len = 0;
        byte[] b = new byte[1024];
        while((len = fileInputStream.read(b))>0){
            outputStream.write(b,0,len);
        }
    }
}

package com.cky.upload.servlet;

import com.cky.upload.bean.FileUploadBean;
import com.cky.upload.db.FileUploadDao;
import com.cky.upload.db.FileUploadDaoImp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetInfoServlet",urlPatterns = "/info")
public class GetInfoServlet extends HttpServlet {
    private FileUploadDao dao = new FileUploadDaoImp();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<FileUploadBean> beans = dao.getAll();
        for(FileUploadBean bean:beans){
            System.out.println("fileName"+bean.getFile_name());
            System.out.println("filePath"+bean.getFile_path());
            System.out.println("id"+bean.getId());
        }
        request.setAttribute("beans",beans);
        request.getRequestDispatcher("/app/download.jsp").forward(request,response);
    }
}

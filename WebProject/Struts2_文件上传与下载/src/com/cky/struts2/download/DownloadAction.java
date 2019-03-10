package com.cky.struts2.download;

import com.cky.struts2.bean.FileUploadBean;
import com.cky.struts2.db.FileUploadDao;
import com.cky.struts2.db.FileUploadDaoImp;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * 文件下载
 *
 */
public class DownloadAction extends ActionSupport implements RequestAware,ModelDriven<FileUploadBean>{

    private String contentType;//文件的类型
    private long contentLength;//下载文件的长度
    private String contentDisposition;//设置content-disposition响应头
    private InputStream inputStream;
    private FileUploadDao dao = new FileUploadDaoImp();
    private Map<String,Object> requestMap = null;
    private FileUploadBean bean = null;

    public String getContentType() {
        return contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public String getContentDisposition() {
        return contentDisposition;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String date(){
        List<FileUploadBean> beans = dao.getAll();
        requestMap.put("beans",beans);
        return "date";
    }

    public String download() throws Exception {
        if(bean!=null){
            System.out.println(bean);
        }
        contentType = "text/html";
        contentDisposition="attachment;filename="+bean.getFileName();
        inputStream = new FileInputStream(bean.getFilePath());
//        inputStream = ServletActionContext.getServletContext().getResourceAsStream(b)
        contentLength = inputStream.available();
        return SUCCESS;
    }

    @Override
    public void setRequest(Map<String, Object> map) {
        requestMap = map;
    }

    @Override
    public FileUploadBean getModel() {
        if(bean==null){
            bean = new FileUploadBean();
        }
        return bean;
    }
}

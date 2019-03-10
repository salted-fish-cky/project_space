package com.cky.struts2.upload;

import com.cky.struts2.bean.FileUploadBean;
import com.cky.struts2.db.FileUploadDao;
import com.cky.struts2.db.FileUploadDaoImp;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.ServletActionContext;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UploadAction extends ActionSupport{
    /**
     * 文件对应的对象
     * private File [fileFieldName];
     * private String [fileFieldName]ContentType;
     * private String [fileFieldName]FileName;
     *
     * 若传递多个文件，则上面的三个属性要改成List类型，多个文件域的name属性值需要一至
     */

    private List<File> file;
    private List<String> desc;
    private List<String> fileFileName;
    private List<String> fileContentType;
    private List<FileUploadBean> fileUploadBeans = new ArrayList<>();
    private FileUploadDao dao = new FileUploadDaoImp();
    public List<File> getFile() {
        return file;
    }

    public void setFile(List<File> file) {
        this.file = file;
    }

    public List<String> getDesc() {
        return desc;
    }

    public void setDesc(List<String> desc) {
        this.desc = desc;
    }

    public List<String> getFileFileName() {
        return fileFileName;
    }

    public void setFileFileName(List<String> fileFileName) {
        this.fileFileName = fileFileName;
    }

    public List<String> getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(List<String> fileContentType) {
        this.fileContentType = fileContentType;
    }


    @Override
    public String execute() throws Exception {
        System.out.println(file);
        System.out.println(fileContentType);
        System.out.println(desc);
        System.out.println(fileFileName);


        if(file!=null){
            for (int i = 0; i <file.size() ; i++) {
                FileUploadBean files = new FileUploadBean();
                files.setFileName(fileFileName.get(i));
                files.setFileDesc(desc.get(i));
                fileUploadBeans.add(files);
            }
        }

        /**
         * 使用io流进行上传
         */

        Random random = new Random();
        ServletContext servletContext = ServletActionContext.getServletContext();
        if (file != null) {

            for (int i = 0; i <file.size() ; i++) {
                String substring = fileUploadBeans.get(i).getFileName().substring(fileUploadBeans.get(i).getFileName().lastIndexOf("."));
                String dir = servletContext.getRealPath("/files/"+System.currentTimeMillis()+random.nextInt(10000)+substring);
                fileUploadBeans.get(i).setFilePath(dir);
                FileOutputStream out = new FileOutputStream(dir);
                FileInputStream in = new FileInputStream(file.get(i));

                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = in.read(buffer))!=-1){
                    out.write(buffer,0,len);
                }
                in.close();
                out.close();



            }

        }
        dao.save(fileUploadBeans);
        return SUCCESS;
    }



}

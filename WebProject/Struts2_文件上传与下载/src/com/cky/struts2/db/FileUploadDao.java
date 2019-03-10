package com.cky.struts2.db;



import com.cky.struts2.bean.FileUploadBean;

import java.util.List;

public interface FileUploadDao {
    public void save(List<FileUploadBean> bean);
    public List<FileUploadBean> getAll();
    public FileUploadBean get(int id);
}

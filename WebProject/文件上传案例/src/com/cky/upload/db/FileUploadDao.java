package com.cky.upload.db;

import com.cky.upload.bean.FileUploadBean;

import java.util.List;

public interface FileUploadDao {
    public void save(List<FileUploadBean> bean);
    public List<FileUploadBean> getAll();
    public FileUploadBean get(int id);
}

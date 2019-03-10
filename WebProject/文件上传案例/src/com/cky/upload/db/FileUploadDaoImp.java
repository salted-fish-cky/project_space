package com.cky.upload.db;

import com.cky.upload.bean.FileUploadBean;

import java.util.List;

public class FileUploadDaoImp extends DAO<FileUploadBean> implements FileUploadDao {
    @Override
    public void save(List<FileUploadBean> beans) {
        String sql = "insert into upload(file_name,file_path,file_desc) values(?,?,?)";
        for(FileUploadBean bean: beans){
            update(sql,bean.getFile_name(),bean.getFile_path(),bean.getFile_desc());
        }
    }

    @Override
    public List<FileUploadBean> getAll() {
        String sql = "select * from upload";

        return getForList(sql);
    }

    @Override
    public FileUploadBean get(int id) {

        String sql = "select * from upload where id=?";
        return get(sql,id);
    }
}

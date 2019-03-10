package com.cky.struts2.db;



import com.cky.struts2.bean.FileUploadBean;

import java.util.List;

public class FileUploadDaoImp extends DAO<FileUploadBean> implements FileUploadDao {
    @Override
    public void save(List<FileUploadBean> beans) {
        String sql = "insert into upload(fileName,filePath,fileDesc) values(?,?,?)";
        for(FileUploadBean bean: beans){
            update(sql,bean.getFileName(),bean.getFilePath(),bean.getFileDesc());
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

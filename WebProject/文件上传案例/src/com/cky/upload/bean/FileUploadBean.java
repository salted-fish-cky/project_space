package com.cky.upload.bean;

public class FileUploadBean {
    private int id;
    private String file_name;
    private String file_path;
    private String file_desc;

    public FileUploadBean(String file_name, String file_path, String file_desc) {
        this.file_name = file_name;
        this.file_path = file_path;
        this.file_desc = file_desc;
    }
    public FileUploadBean(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_desc() {
        return file_desc;
    }

    public void setFile_desc(String file_desc) {
        this.file_desc = file_desc;
    }
}

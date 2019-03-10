package com.cky.struts2.bean;


import java.io.File;

public class FileUploadBean {
    private String id;
    private String fileName;
    private String filePath;
    private String fileDesc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc;
    }

    public FileUploadBean(String fileName, String filePath, String fileDesc) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileDesc = fileDesc;
    }

    public FileUploadBean(){}

    @Override
    public String toString() {
        return "FileUploadBean{" +
                "id='" + id + '\'' +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileDesc='" + fileDesc + '\'' +
                '}';
    }
}

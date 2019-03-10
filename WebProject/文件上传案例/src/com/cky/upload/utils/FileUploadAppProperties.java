package com.cky.upload.utils;

import java.util.HashMap;
import java.util.Map;

public class FileUploadAppProperties {

    private Map<String,String> property = new HashMap<>();
    private static FileUploadAppProperties instance = new FileUploadAppProperties();
    private FileUploadAppProperties(){}

    public static FileUploadAppProperties getInstance() {
        return instance;
    }

    public void addProperty(String propertyName,String propertyValue){
        property.put(propertyName,propertyValue);
    }
    public String getProperty(String propertyName){
        return property.get(propertyName);
    }
}

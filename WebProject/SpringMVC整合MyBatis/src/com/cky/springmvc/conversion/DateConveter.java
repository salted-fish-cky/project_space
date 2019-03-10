package com.cky.springmvc.conversion;

import org.springframework.core.convert.converter.Converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 转换日期数据
 */
public class DateConveter implements Converter<String,Date> {
    @Override
    public Date convert(String s) {
        try {
            if(s != null){//2014:11-09 11_23-44
                DateFormat dateFormat = new SimpleDateFormat("yyyy:MM-dd HH_mm-ss");

                return dateFormat.parse(s);
            }
        }catch (Exception e){

        }
        return null;
    }
}

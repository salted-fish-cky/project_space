package com.cky.demo.struts2;

import org.apache.struts2.util.StrutsTypeConverter;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

public class DateConverter extends StrutsTypeConverter {

    private DateFormat dateFormat;
    public DateConverter(){
        System.out.println("DateConverter");
        dateFormat = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
    }
    @Override
    public Object convertFromString(Map map, String[] strings, Class aClass) {
        System.out.println("convertFromString");
        if(aClass == Date.class){
            if(strings!=null&&strings.length>0){
                String value = strings[0];

                try{
                    return dateFormat.parseObject(value);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        //若没有转换成功则返回strings
        return strings;
    }

    @Override
    public String convertToString(Map map, Object o) {
        System.out.println("convertToString");
        if(o instanceof java.util.Date){
            Date date = (Date) o;
            System.out.println(dateFormat.format(date));
            return dateFormat.format(date);
        }

        return null;

    }
}

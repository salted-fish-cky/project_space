package com.cky.sparkproject.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置管理组件
 *
 * 1、配置管理组件可以复杂，也可以很简单，对于简单的配置管理组件来说，只要开发一个类，可以在第一次访问它的
 * 		时候，就从对应的properties文件中，读取配置项，并提供外界获取某个配置key对应的value的方法
 * 2、如果是特别复杂的配置管理组件，那么可能需要使用一些软件设计中的设计模式，比如单例模式、解释器模式
 * 		可能需要管理多个不同的properties，甚至是xml类型的配置文件
 * 3、我们这里的话，就是开发一个简单的配置管理组件，就可以了
 *
 * @author Administrator
 *
 */
public class ConfigurationManager {

    private static Properties properties = new Properties();

    static {
        try {
            InputStream in = ConfigurationManager.class.getClassLoader().getResourceAsStream("my.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }

    public static Integer getInteger(String key){
        String value = properties.getProperty(key);
        try {
            return Integer.valueOf(value);
        }catch (Exception e){

        }
        return 0;
    }


    public static Boolean getBoolean(String key){
        String value = properties.getProperty(key);
        try{
            return Boolean.valueOf(value);
        }catch (Exception e){
            e.printStackTrace();
        }

        return true;

    }

    public static Long getLong(String key){
        String value = properties.getProperty(key);
        try{
            return Long.valueOf(value);
        }catch (Exception e){
            e.printStackTrace();
        }

        return 0L;
    }
}

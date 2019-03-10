package cn.e3mall.cart.springconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"resource.properties"})
public class E3Application {

    public static void main(String[] args){

    }
}

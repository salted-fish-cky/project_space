package com.cky.config;

import com.cky.interceptor.MiniInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Value("${com.cky.resourceSpace}")
    private String RESOURCE_SPACE;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       registry.addResourceHandler("/**")
               .addResourceLocations("file:"+RESOURCE_SPACE)
               .addResourceLocations("classpath:/META-INF/resources/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(miniInterceptor()).addPathPatterns("/user/**")
                .addPathPatterns("/video/upload","/video/userLike","/video/userUnLike","/video/saveComment")
                .addPathPatterns("/bgm/**").excludePathPatterns("/user/queryPublisher");
    }

    @Bean
    public MiniInterceptor miniInterceptor(){
        return  new MiniInterceptor();
    }

    @Bean(initMethod = "init")
    public ZKCuratorClient zKCuratorClient(){
        return  new ZKCuratorClient();
    }


}

package com.cky.interceptor;

import com.cky.utils.IMoocJSONResult;
import com.cky.utils.JsonUtils;
import com.cky.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class MiniInterceptor implements HandlerInterceptor {

    @Autowired
    public RedisOperator redisOperator;

    public static final String USER_REDIS_SESSION = "user-redis-session";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String userId = request.getHeader("userId");
        String userToken = request.getHeader("userToken");

        if(StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)){
            String uniqueToken = redisOperator.get(USER_REDIS_SESSION + ":" + userId);
            if(StringUtils.isEmpty(uniqueToken) && StringUtils.isBlank(uniqueToken)){
                System.out.println("请登录...");
                returnErrorResponse(response,IMoocJSONResult.errorTokenMsg("请登录..."));
                return false;
            }else {
                if(!userToken.equals(uniqueToken)){
                    System.out.println("账号被挤出...");
                    returnErrorResponse(response,IMoocJSONResult.errorTokenMsg("账号被挤出..."));
                    return false;
                }
            }
        }else {
            System.out.println("请登录...");
            returnErrorResponse(response,IMoocJSONResult.errorTokenMsg("请登录..."));

            return false;
        }
        System.out.println(".......");
        return true;

    }


    public void returnErrorResponse(HttpServletResponse response, IMoocJSONResult iMoocJSONResult) throws IOException {
        OutputStream out = null;

        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(iMoocJSONResult).getBytes("utf-8"));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(out != null){
                out.close();
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }



}

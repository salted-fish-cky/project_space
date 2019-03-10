package com.cky.interceptor;

import com.cky.pojo.AdminUser;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class LoginInterceptor implements HandlerInterceptor {

    private List<String> unCheckUrls;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String url = request.getRequestURI();
        url = url.replaceAll(request.getContextPath(), "");
        if(unCheckUrls.contains(url)){
            return true;
        }
        AdminUser user = (AdminUser) request.getSession().getAttribute("user");
        if(user == null){
            response.sendRedirect(request.getContextPath()+"/users/login.action");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    public void setUnCheckUrls(List unCheckUrls) {
        this.unCheckUrls = unCheckUrls;
    }

    public List getUnCheckUrls() {
        return unCheckUrls;
    }
}

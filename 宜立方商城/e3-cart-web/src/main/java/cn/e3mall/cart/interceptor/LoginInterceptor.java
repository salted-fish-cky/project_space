package cn.e3mall.cart.interceptor;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor{
    @Autowired
    private TokenService tokenService;

    //前处理，执行handler之前执行此方法 返回 true，放行   false：拦截
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {

        //1.从cookie中取token
        String token = CookieUtils.getCookieValue(httpServletRequest, "token");
        //2.如果没有token，未登录状态，直接放行
        if(StringUtils.isBlank(token)){
            return true;
        }
        //3.取到token，需要调用sso系统的服务，根据token取用户信息
        E3Result result = tokenService.getUserByToken(token);
        //4.没有取到用户信息，登录过期，直接放行
        if(result.getStatus() != 200){
            return true;
        }
        //5.取到用户信息，登录状态
        TbUser user = (TbUser) result.getData();
        //6.把用户信息放到request中，只需要在Controller中判断request中是否包含user信息
        httpServletRequest.setAttribute("user",user);
        return true;
    }

    //handler执行之后，返回ModelAndView之前
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    //完成处理，返回ModelAndView之后，可以在此处理异常
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}

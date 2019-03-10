package com.cky.demo.tag;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * 自定义标签比较大小实现类
 */
public class MaxTag extends SimpleTagSupport {

    private String num1;
    private String num2;

    public void setNum1(String num1) {
        this.num1 = num1;
    }

    public void setNum2(String num2) {
        this.num2 = num2;
    }

    @Override
    public void doTag() throws JspException, IOException {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        try{
            int a = Integer.parseInt(num1);
            int b = Integer.parseInt(num2);
            System.out.println("a="+a+"b="+b);
            out.print(a>b?a:b);
        }catch (Exception e){
            out.write("输入的格式不正确");
        }
    }
}

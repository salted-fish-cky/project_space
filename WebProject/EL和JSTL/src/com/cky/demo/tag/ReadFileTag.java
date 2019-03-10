package com.cky.demo.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.Buffer;

/**
 * 自定义读取文件的标签
 */
public class ReadFileTag extends SimpleTagSupport {

    private String src;

    public void setSrc(String src) {
        this.src = src;
    }

    @Override
    public void doTag() throws JspException, IOException {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        InputStream in = pageContext.getServletContext().getResourceAsStream(src);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
        String str = "";
        while((str = buffer.readLine())!=null){
            out.write(str);
            out.write("<br>");
        }
    }
}

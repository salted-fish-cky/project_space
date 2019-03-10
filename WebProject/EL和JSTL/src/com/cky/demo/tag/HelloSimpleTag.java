package com.cky.demo.tag;

import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTag;
import java.io.IOException;

/**
 * 自定义标签实现的接口
 */


/**
 * 自定义标签的步骤：
 * 1.创建一个标签处理器类：实现SimpleTag接口
 * 2.在WEB-INF文件夹下新建.tld为扩展名xml文件：例如：mytag.tld
 * 3.在.tld文件中描述自定义标签
 * 4.在JSP页面上使用自定义标签  例如：test.jsp
 */


public class HelloSimpleTag implements SimpleTag {
    @Override
    public void doTag() throws JspException, IOException {
        System.out.println("doTag");
    }

    @Override
    public void setParent(JspTag jspTag) {
        System.out.println("setParent");
    }

    @Override
    public JspTag getParent() {
        System.out.println("getParent");
        return null;
    }

    @Override
    public void setJspContext(JspContext jspContext) {
        System.out.println("setJspContext");
    }

    @Override
    public void setJspBody(JspFragment jspFragment) {
        System.out.println("setJspBody");
    }
}

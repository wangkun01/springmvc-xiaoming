package com.xiaoming.spring.framework.webmvc.servlet;

import com.xiaoming.spring.framework.context.XMApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: springmvcProjects
 * @description:
 * @author: xiaoming
 * @create: 2020-01-09 23:44
 */
public class XMDispatherServletTest extends HttpServlet {

    private final String LOCATION = "contextConfigLocation";
    
    @Override
    public void init(ServletConfig config) throws ServletException {

        XMApplicationContext xmApplicationContext = new XMApplicationContext(config.getInitParameter(LOCATION));
        //xmApplicationContext.refresh();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
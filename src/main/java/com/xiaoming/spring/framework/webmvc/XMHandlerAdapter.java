package com.xiaoming.spring.framework.webmvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 为什么要把mappedHandler传进来?因为mappedHandler包含了controller,method,url信息
 * @author wangkun
 * @date 2019-08-03 23:47
 */
public class XMHandlerAdapter {
    public XMModelAndView handle(HttpServletRequest req, HttpServletResponse resp, XMHandlerMapping mappedHandler) {
        //根据用户请求的req参数信息跟method的参数动态匹配
        //resp传进来的目的只有一个：为了将其赋值给方法的参数,仅此而已
        return null;
    }
}

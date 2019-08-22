package com.xiaoming.spring.framework.webmvc;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author wangkun
 * @date 2019-08-03 23:47
 */

public class XMHandlerMapping {
    //url是一个正则表达式
    private Pattern url;
    private Object controller;
    private Method method;

    public XMHandlerMapping(Pattern url, Object controller, Method method) {
        this.url = url;
        this.controller = controller;
        this.method = method;
    }

    public Pattern getUrl() {
        return url;
    }

    public void setUrl(Pattern url) {
        this.url = url;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}

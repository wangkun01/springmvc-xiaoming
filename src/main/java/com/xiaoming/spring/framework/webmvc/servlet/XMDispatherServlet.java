package com.xiaoming.spring.framework.webmvc.servlet;

import com.xiaoming.spring.framework.context.XMApplicationContext;
import com.xiaoming.spring.framework.webmvc.XMHandlerAdapter;
import com.xiaoming.spring.framework.webmvc.XMHandlerMapping;
import com.xiaoming.spring.framework.webmvc.XMModelAndView;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangkun
 * @date 2019-07-31 22:52
 */
//DispatherServlet作为mvc启动的入口
public class XMDispatherServlet extends HttpServlet {

    private final  String LOCATION = "contextConfigLocation";
    //private Map<String, XMHandlerMapping> handlerMapping = new HashMap<String, XMHandlerMapping>();

    //
    List<XMHandlerMapping> handlerMappings = new ArrayList<XMHandlerMapping>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        //相当于把IOC容器初始化
        XMApplicationContext context = new XMApplicationContext(config.getInitParameter(LOCATION));

        initStrategies(context);

    }


    protected void initStrategies(XMApplicationContext context) {

        //九大组件

        //文件上传解析
        initMultipartResolver(context);
        //本地化解析
        initLocaleResolver(context);
        //主题解析
        initThemeResolver(context);

        /**
         * 将controller中配置的RequestMapping和Method进行一一对应
         */
        //通过handlerMapping将请求映射到处理器（自己实现逻辑）
        initHandlerMappings(context);

        /**
         * 用来动态匹配Method参数，包括类型转换和动态赋值
         */
        //通过handlerAdapter进行多类型的参数动态匹配（自己实现逻辑）
        initHandlerAdapters(context);
        //如果执行过程中遇到异常交给handlerExceptionResolver处理
        initHandlerExceptionResolvers(context);
        //直接解析请求到视图
        initRequestToViewNameTranslator(context);

        /**
         *自己解析一套模板语言
         */
        //通过ViewResolver解析逻辑视图到具体视图实现（自己实现逻辑））
        initViewResolvers(context);

        //flash映射管理器
        initFlashMapManager(context);
    }

    private void initMultipartResolver(XMApplicationContext context) {
    }

    private void initLocaleResolver(XMApplicationContext context) {
    }

    /**
     * 将controller中配置的RequestMapping和Method进行一一对应
     * @param context
     */
    private void initThemeResolver(XMApplicationContext context) {

    }

    private void initHandlerMappings(XMApplicationContext context) {

    }

    private void initHandlerAdapters(XMApplicationContext context) {
    }

    private void initHandlerExceptionResolvers(XMApplicationContext context) {
    }

    private void initRequestToViewNameTranslator(XMApplicationContext context) {
    }

    private void initViewResolvers(XMApplicationContext context) {
    }


    private void initFlashMapManager(XMApplicationContext context) {
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //doDispatch(req,resp);
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replaceAll(contextPath," ");

        XMHandlerMapping hander = handlerMapping.get(url);
        try {
            XMModelAndView mv = (XMModelAndView)hander.getMethod().invoke(hander.getController(),null );
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) {
        XMHandlerMapping mappedHandler =  getHandler(req);

        XMHandlerAdapter ha = getHandlerAdapter(mappedHandler);

         XMModelAndView mv = ha.handle(req,resp,mappedHandler);

         processDispatchResult(req,mv);
    }

    private void processDispatchResult(HttpServletRequest req, XMModelAndView mv) {
        //这里会调用viewResolver
    }

    private XMHandlerAdapter getHandlerAdapter(XMHandlerMapping handler) {
        return null ;
    }

    private XMHandlerMapping getHandler(HttpServletRequest req) {
        return null ;
    }
}

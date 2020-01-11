package com.xiaoming.spring.framework.context;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.xiaoming.spring.demo.action.MyAction;
import com.xiaoming.spring.framework.annotation.XMAutowired;
import com.xiaoming.spring.framework.annotation.XMController;
import com.xiaoming.spring.framework.annotation.XMService;
import com.xiaoming.spring.framework.beans.XMBeanDefinition;
import com.xiaoming.spring.framework.beans.XMBeanPostProcessor;
import com.xiaoming.spring.framework.beans.XMBeanWrapper;
import com.xiaoming.spring.framework.core.XMBeanFactory;
import com.xiaoming.spring.support.XMBeanDefinitionReader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 小明版的IOC容器,仿ClassPathXmlApplicationContext
 * @author wangkun
 * @date 2019-08-01 00:01
 */

public class XMApplicationContext implements XMBeanFactory {

    private  String[] locations;

    private XMBeanDefinitionReader xmBeanDefinitionReader ;

    //保存单例bean,注册式
    private Map<String,Object> beanCacheMap= new ConcurrentHashMap<String, Object>();

    //用户保存配置信息，key是beanName
    private Map<String, XMBeanDefinition> beanDifinitionMap = new ConcurrentHashMap<String, XMBeanDefinition>();

    //用来存储所有代理过的对象
    private Map<String,XMBeanWrapper> beanWrapperMap = new ConcurrentHashMap<String,XMBeanWrapper>();


    public XMApplicationContext(String ... locations){
        this.locations = locations;
        refresh();
    }

    //1.通过读取BeanDefinition中的信息
    //2.然后通过反射创建一个实例返回
    //Spring做法是：不会把原始的对象放出去,会用一个beanWrapper来进行一次包装
    //装饰器模式：1.保留原来的oop关系2.我需要对他进行扩展(为以后AOP打基础)
    @Override
    public Object getBean(String beanName){

        try {
            //生成通知事件
            XMBeanPostProcessor xmBeanPostProcessor = new XMBeanPostProcessor();

            Object instance;
            XMBeanDefinition xmBeanDefinition = beanDifinitionMap.get(beanName);
            instance = instantionBean(xmBeanDefinition);
            if (instance == null) {
                return null;
            }
            //在实例初始化完成之前调用一次
            xmBeanPostProcessor.postProcessBeforeInitialization(instance,beanName);

            XMBeanWrapper xmBeanWrapper = new XMBeanWrapper(instance);
            //用来存储所有代理过的对象
            this.beanWrapperMap.put(beanName,xmBeanWrapper);

            xmBeanWrapper.setXmBeanPostProcessor(xmBeanPostProcessor);

            //在实例初始化完成之后调用一次
            xmBeanPostProcessor.postProcessAfterInitialization(instance,beanName);

            //真正注入的方法
            populateBean(beanName,instance);

            //通过这么调用给我们预留了可操作的空间
            return this.beanWrapperMap.get(beanName).getWrappedInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void populateBean(String beanName,Object instance){

        Class<?> clazz = instance.getClass();
        if(!(clazz.isAnnotationPresent(XMController.class) || clazz.isAnnotationPresent(XMService.class))){
            return;
        }

        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            if(!field.isAnnotationPresent(XMAutowired.class)){
                continue;
            }

            XMAutowired xmAutowired = field.getAnnotation(XMAutowired.class);
            String autowiredBeanName = xmAutowired.value().trim();
            if("".equals(autowiredBeanName)){
                autowiredBeanName = field.getName();
            }
            field.setAccessible(true);
            try {
                System.out.println("===" + instance + "==" +  beanWrapperMap + "="+autowiredBeanName);
                field.set(instance,this.beanWrapperMap.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


        }


    }

    //传一个beanDefinition返回一个实例bean
    private Object instantionBean(XMBeanDefinition xmBeanDefinition){

        Object instance;
        try {
            synchronized (this){
                String className = xmBeanDefinition.getBeanName();
                if(this.beanCacheMap.containsKey(className)){
                    instance = beanCacheMap.get(className);
                }else{
                    //反射创建实例
                    Class<?> clazz = Class.forName(className);
                    instance = clazz.newInstance();
                    this.beanCacheMap.put(className,clazz);
                }
                return instance;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null ;
    }


    public  void refresh(){

        //定位--配置的包扫描路径,读取所有扫描包路径下的class,保存到一个List集合中
        this.xmBeanDefinitionReader = new XMBeanDefinitionReader(locations);

        //加载 --获取上一步保存的registryBeanClasses List集合 格式com.xiaoming.Hello
        List<String> beanDefinitions =  xmBeanDefinitionReader.loadBeanDefinitions();

        //注册 真正的将BeanDefinition注册到BeandefinitionMap中
        doRigister(beanDefinitions);

        //依赖注入(lazy-init=false),首先要实例化,在这里调用getBean方法
        doDependencyInjection();

        //临时测试代码
        MyAction myAction = null ;
        try {

             Object o = this.getBean("myAction");
             if(o instanceof  MyAction){
                 System.out.println("是真实的！");
             }



        }catch (Exception e){
            e.printStackTrace();

        }


        myAction.query(null,null, "xiaoming");



    }

    //开始自动化的依赖注入
    private void doDependencyInjection() {

        for(Map.Entry<String, XMBeanDefinition> entry : this.beanDifinitionMap.entrySet()){
            String beanName = entry.getKey();
            //如果是延迟加载
            if(!entry.getValue().isLazyInit()){
                getBean(beanName);
            }
        }

        //依赖注入临时解决方案
        for(Map.Entry<String,XMBeanWrapper> beanWrapperEntry : this.beanWrapperMap.entrySet()){

            populateBean(beanWrapperEntry.getKey(),beanWrapperEntry.getValue().getOriginalInstance());

        }


    }

    /**
     * 真正的将beandifinition注册到beandifinitionMap中
     */
    private void doRigister(List<String> beanDefinitions) {
        //beanName 有三种情况
        //1.默认首字母小写
        //2.自定义名字
        //3.接口注入
        try {
            for(String className : beanDefinitions ){
                Class<?> beanClass = Class.forName(className) ;
                //接口不能实例化的,注入的是它的实现类
                if(beanClass.isInterface()){
                    continue;
                }
                XMBeanDefinition xmBeanDefinition = xmBeanDefinitionReader.registerBean(className);
                if(xmBeanDefinition != null ){
                    this.beanDifinitionMap.put(xmBeanDefinition.getFactoryBeanName(),xmBeanDefinition);
                }

                //注入bean时,如果是实现类,把接口也注入到ioc容器中（key:bean在IOC中的名字，默认的首字母小写，value:当前实现类的BeanDefinition）
                Class<?>[] interfaces =  beanClass.getInterfaces();
                for(Class<?> i: interfaces ){
                    //如果是多个实现类,只能覆盖,会报错
                    //这个时候可以自定义名字
                    this.beanDifinitionMap.put(i.getName(),xmBeanDefinition);
                }
                //到这里为止容器初始化完毕

            }
        }catch (Exception e){
            e.printStackTrace();

        }
    }

}

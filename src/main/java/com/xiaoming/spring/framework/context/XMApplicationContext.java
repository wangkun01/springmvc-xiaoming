package com.xiaoming.spring.framework.context;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.xiaoming.spring.framework.beans.XMBeanDefinition;
import com.xiaoming.spring.framework.core.XMBeanFactory;
import com.xiaoming.spring.support.XMBeanDefinitionReader;
import org.springframework.beans.BeansException;
import org.springframework.util.StringUtils;

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

    //保存单例bean
    private Map<String,Object> beanCacheMap= new ConcurrentHashMap<String, Object>();

    //用户保存配置信息
    private Map<String, XMBeanDefinition> beanDifinitionMap = new ConcurrentHashMap<String, XMBeanDefinition>();


    public XMApplicationContext(String ... locations){
        refresh();
        this.locations = locations;
    }

    //通过读取BeanDefinition中的信息
    //然后通过反射创建一个实例返回
    //Spring做法是：不会把原始的对象放出去,会用一个beanWrapper来进行一次包装
    //装饰器模式：1.保留原来的oop关系2.我需要对他进行扩展,为以后AOP打基础
    @Override
    public Object getBean(String beanName) throws BeansException {
        Object instance ;
        XMBeanDefinition xmBeanDefinition = beanDifinitionMap.get(beanName);
        instance = instantionBean(xmBeanDefinition);
        return null;
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

        //加载 --获取上一步保存的List集合
        List<String> beanDefinitions =  xmBeanDefinitionReader.loadBeanDefinitions();

        //注册
        doRigister(beanDefinitions);

        //依赖注入(lazy-init=false),首先要实例化


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

                //获取beanClass所实现的接口,接口的bean注入的是实现类
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

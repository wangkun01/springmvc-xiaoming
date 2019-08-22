package com.xiaoming.spring.framework.beans;

/**
 * Bean包装类：为了扩展，原生spring是一个接口，此处是为了方便
 * @author wangkun
 * @date 2019-08-19 22:43
 */
public class XMBeanWrapper {

    //经过包装的类
    private Object wrappedInstance;
    //原生的类
    private Object originInstance;

    public XMBeanWrapper(Object instance){
        //暂时把这两个类赋值为一样
        this.wrappedInstance = instance;
        this.originInstance = instance ;
    }

    public Object getWrappedInstance(){
        return wrappedInstance;
    }

    //返回代理以后的class
    //$proxy0
    public Class<?> getWrappedClass(){
     return this.wrappedInstance.getClass();
    }
}

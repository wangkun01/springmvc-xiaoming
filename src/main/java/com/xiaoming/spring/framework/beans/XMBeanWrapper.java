package com.xiaoming.spring.framework.beans;

/**
 * Bean包装类：为了扩展，原生spring是一个接口，此处是为了方便
 * @author wangkun
 * @date 2019-08-19 22:43
 */
public class XMBeanWrapper extends XMFactoryBean {

    //观察者模式：支持事件响应，会有一监听
    private XMBeanPostProcessor xmBeanPostProcessor;

    //经过包装的对象
    private Object wrappedInstance;
    //原生的对象,通过反射new出来,要包装起来保存
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

    public XMBeanPostProcessor getXmBeanPostProcessor() {
        return xmBeanPostProcessor;
    }

    public void setXmBeanPostProcessor(XMBeanPostProcessor xmBeanPostProcessor) {
        this.xmBeanPostProcessor = xmBeanPostProcessor;
    }

    public Object getOriginalInstance() {
        return originInstance;
    }
}

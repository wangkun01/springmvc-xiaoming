package com.xiaoming.spring.framework.beans;

/**
 * 用于做事件监听的
 * @author wangkun
 * @date 2019-09-01 21:39
 */
public class XMBeanPostProcessor {

    public Object postProcessBeforeInitialization(Object bean, String beanName){
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName){
        return bean;
    }
}

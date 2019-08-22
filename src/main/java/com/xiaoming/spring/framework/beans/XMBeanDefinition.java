package com.xiaoming.spring.framework.beans;

import com.sun.istack.internal.Nullable;

/**
 * BeanDefinition 存储配置文件的信息，相当于保存在内存中的配置
 * @author wangkun
 * @date 2019-08-19 22:43
 */
public class XMBeanDefinition {

    private String beanName;
    private boolean lazyInit = false;
    //这么命名是工厂产生的bean
    private String factoryBeanName;

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }
}

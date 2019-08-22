package com.xiaoming.spring.framework.core;

import org.springframework.beans.BeansException;

/**
 * @author wangkun
 * @date 2019-08-08 23:51
 */
public interface XMBeanFactory {

    //根据bean的名字获取IOC中bean实例
    Object getBean(String beaName) throws BeansException;
}

package com.xiaoming.spring.support;

import com.xiaoming.spring.framework.beans.XMBeanDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * BeanDefinitionReader对配置文件进行查找、读取和解析
 * @author wangkun
 * @date 2019-08-19 22:39
 */
public class XMBeanDefinitionReader {

    private Properties config = new Properties();

    private List<String> registryBeanClasses = new ArrayList<String>();

    //配置文件中的自动包扫描key
    private final String SCAN_PACKAGE = "scanPackage";


    //读取spring的配置文件，对配置文件进行查找、读取、结息,获取到配置的包扫描路径
    public XMBeanDefinitionReader(String... locations){
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(locations[0].replace("classpath:",""));
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is != null ){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //获取自动扫描的包名
        doLoadBeanDefinitions(config.getProperty(SCAN_PACKAGE));

    }

    public List<String> loadBeanDefinitions(){
        return this.registryBeanClasses ;
    }

    //每注册一个classname就返回一个BeanDefinition
    public XMBeanDefinition registerBean(String beanName){
        if(registryBeanClasses.contains(beanName)){
            XMBeanDefinition beanDefinition = new XMBeanDefinition();
            beanDefinition.setBeanName(beanName);
            //FactoryBeanName bean在IOC中的名字，默认的首字母小写
            beanDefinition.setFactoryBeanName(lowerFirstCase(beanName.substring(beanName.lastIndexOf(".")+1)));
            return beanDefinition;
        }
        return null ;
    }

    public Properties getConfig(){
        return this.config;
    }

    //递归扫描自动扫描包下相关的class,并且保存到List中
    private void doLoadBeanDefinitions(String packageName){
        URL url = this.getClass().getClassLoader().getResource("/" + packageName.replaceAll("\\.","/"));
        File classDir = new File(url.getFile());
        for(File file : classDir.listFiles()){
            if(file.isDirectory()){
                doLoadBeanDefinitions(packageName+ "."+file.getName());
            }else{
                registryBeanClasses.add(packageName + "." + file.getName().replaceAll(".class",""));
            }
        }

    }

    //首字母小写的方法
    private String lowerFirstCase(String str){
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}

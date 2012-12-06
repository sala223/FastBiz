package com.fastbiz.core.web.context;

import java.util.Map;
import javax.jws.WebService;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import org.apache.cxf.Bus;
import org.apache.cxf.common.util.ClassHelper;
import org.apache.cxf.databinding.DataBinding;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class JAXWsWebServicePublisherBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware{

    private static final Logger LOG         = LoggerFactory.getLogger(JAXWsWebServicePublisherBeanPostProcessor.class);

    private String              prototypeDataBindingBeanName;

    private String              prototypeServerFactoryBeanName;

    private BeanFactory         beanFactory;

    private boolean             customizedServerFactory;

    private boolean             customizedDataBinding;

    private String              urlPrefix   = "/ws/";

    private String              busBeanName = "cxf";

    private Bus                 bus;

    public void setUrlPrefix(String urlPrefix){
        this.urlPrefix = urlPrefix;
    }

    public void setBusBeanName(String busBeanName){
        this.busBeanName = busBeanName;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException{
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException{
        Class<?> clazz = ClassHelper.getRealClass(bean);
        if (clazz.isAnnotationPresent(WebService.class)) {
            WebService ws = clazz.getAnnotation(WebService.class);
            String url = urlPrefix + ws.serviceName();
            String fmt = "publish endpoint %s to %s";
            LOG.info(String.format(fmt, clazz.getName(), url));
            createAndPublishEndpoint(url, bean);
        }
        return bean;
    }

    private void createAndPublishEndpoint(String url, Object implementor){
        ServerFactoryBean serverFactory = null;
        if (prototypeServerFactoryBeanName != null) {
            if (!beanFactory.isPrototype(prototypeServerFactoryBeanName)) {
                throw new IllegalArgumentException("prototypeServerFactoryBeanName must indicate a scope='prototype' bean");
            }
            serverFactory = beanFactory.getBean(prototypeServerFactoryBeanName, ServerFactoryBean.class);
            customizedServerFactory = true;
        } else {
            serverFactory = new JaxWsServerFactoryBean();
        }
        serverFactory.setServiceBean(implementor);
        serverFactory.setServiceClass(ClassHelper.getRealClass(implementor));
        serverFactory.setAddress(url);
        DataBinding dataBinding = null;
        if (prototypeDataBindingBeanName != null) {
            if (!beanFactory.isPrototype(prototypeDataBindingBeanName)) {
                throw new IllegalArgumentException("prototypeDataBindingBeanName must indicate a scope='prototype' bean");
            }
            customizedDataBinding = true;
            dataBinding = beanFactory.getBean(prototypeDataBindingBeanName, DataBinding.class);
        } else {
            dataBinding = new JAXBDataBinding();
        }
        serverFactory.setDataBinding(dataBinding);
        serverFactory.setBus(bus);
        serverFactory.create();
    }

    public void setServletConfig(ServletConfig servletConfig){
        ServletContext servletContext = servletConfig.getServletContext();
        ApplicationContext parent = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        while (parent != null) {
            Map<String, Object> beans = parent.getBeansWithAnnotation(WebService.class);
            for (String beanName : beans.keySet()) {
                this.postProcessAfterInitialization(beans.get(beanName), beanName);
            }
            parent = parent.getParent();
        }
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException{
        this.beanFactory = beanFactory;
        Object bean = beanFactory.getBean(busBeanName);
        if (bean == null || !(bean instanceof Bus)) {
            throw new NullPointerException("cannot get bus bean from bean name " + busBeanName);
        }
        this.bus = (Bus) bean;
    }

    public String getPrototypeServerFactoryBeanName(){
        return prototypeServerFactoryBeanName;
    }

    public void setPrototypeServerFactoryBeanName(String prototypeServerFactoryBeanName){
        this.prototypeServerFactoryBeanName = prototypeServerFactoryBeanName;
    }

    public String getPrototypeDataBindingBeanName(){
        return prototypeDataBindingBeanName;
    }

    public void setPrototypeDataBindingBeanName(String prototypeDataBindingBeanName){
        this.prototypeDataBindingBeanName = prototypeDataBindingBeanName;
    }

    public boolean isCustomizedServerFactory(){
        return customizedServerFactory;
    }

    public boolean isCustomizedDataBinding(){
        return customizedDataBinding;
    }
}

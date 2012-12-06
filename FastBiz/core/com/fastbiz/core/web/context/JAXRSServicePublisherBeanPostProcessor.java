package com.fastbiz.core.web.context;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Path;
import org.apache.cxf.common.util.ClassHelper;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.JAXRSServiceFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class JAXRSServicePublisherBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware{

    private static final Logger    LOG = LoggerFactory.getLogger(JAXRSServicePublisherBeanPostProcessor.class);

    private JAXRSServerFactoryBean serverBean;

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException{
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException{
        Class<?> clazz = ClassHelper.getRealClass(bean);
        if (clazz.isAnnotationPresent(Path.class)) {
            Path ws = clazz.getAnnotation(Path.class);
            String fmt = "publish RS Service %s to %s";
            LOG.info(String.format(fmt, clazz.getName(), ws.value()));
            createAndPublishRSService(bean);
        }
        return bean;
    }

    private void createAndPublishRSService(Object bean){
        JAXRSServiceFactoryBean serviceFactory = serverBean.getServiceFactory();
        List<Object> beans = new ArrayList<Object>();
        beans.add(bean);
        serviceFactory.setResourceClassesFromBeans(beans);
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException{
        serverBean = beanFactory.getBean(JAXRSServerFactoryBean.class);
        if (serverBean == null) {
            throw new NullPointerException("JAXRSServerFactoryBean bean is not defined");
        }
    }
}

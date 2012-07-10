package com.fastbiz.core.solution.ioc;

public class DefaultBeanFactory implements BeanFactory{

    private org.springframework.beans.factory.BeanFactory delegator;

    public DefaultBeanFactory(org.springframework.beans.factory.BeanFactory delegator) {
        this.delegator = delegator;
    }

    @Override
    public Object getBean(String beanName){
        return delegator.getBean(beanName);
    }

    @Override
    public Object getBean(String beanName, Object ... args){
        return delegator.getBean(beanName, args);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> beanType){
        return delegator.getBean(beanName, beanType);
    }

    @Override
    public boolean containsBean(String name){
        return delegator.containsBean(name);
    }

    @Override
    public <T> T getBean(Class<T> beanType){
        return delegator.getBean(beanType);
    }
}

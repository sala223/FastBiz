package com.fastbiz.core.solution.ioc;

import org.springframework.beans.factory.BeanFactory;

public class SpringBeanFactoryAdapter implements BeanContainer{

    private BeanFactory delegator;

    public SpringBeanFactoryAdapter(BeanFactory delegator) {
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

    public Object unwrap(){
        return delegator;
    }
}

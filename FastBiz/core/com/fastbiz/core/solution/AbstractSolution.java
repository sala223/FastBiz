package com.fastbiz.core.solution;

import com.fastbiz.core.solution.ioc.BeanContainer;

;
public abstract class AbstractSolution implements Solution{

    @Override
    public Object getBean(String beanName){
        return getBeanContainer().getBean(beanName);
    }

    @Override
    public Object getBean(String beanName, Object ... args){
        return getBeanContainer().getBean(beanName, args);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> beanType){
        return getBeanContainer().getBean(beanName, beanType);
    }

    @Override
    public boolean containsBean(String name){
        return getBeanContainer().containsBean(name);
    }

    @Override
    public <T> T getBean(Class<T> beanType){
        return getBeanContainer().getBean(beanType);
    }

    protected abstract BeanContainer getBeanContainer();
}

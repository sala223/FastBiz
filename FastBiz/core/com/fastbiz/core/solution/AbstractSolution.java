package com.fastbiz.core.solution;

import com.fastbiz.core.solution.ioc.BeanFactory;

public abstract class AbstractSolution implements Solution{

    protected BeanFactory beanFactory;

    protected String      solutionId;

    public String getSolutionId(){
        return solutionId;
    }

    public void setSolutionId(String solutionId){
        this.solutionId = solutionId;
    }

    @Override
    public Object getBean(String beanName){
        return getBeanFactory().getBean(beanName);
    }

    @Override
    public Object getBean(String beanName, Object ... args){
        return getBeanFactory().getBean(beanName, args);
    }

    @Override
    public <T> T getBean(String beanName, Class<T> beanType){
        return getBeanFactory().getBean(beanName, beanType);
    }

    @Override
    public boolean containsBean(String name){
        return getBeanFactory().containsBean(name);
    }

    @Override
    public <T> T getBean(Class<T> beanType){
        return getBeanFactory().getBean(beanType);
    }

    protected abstract BeanFactory getBeanFactory();
}

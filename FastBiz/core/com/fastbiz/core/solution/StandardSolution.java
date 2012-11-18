package com.fastbiz.core.solution;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import com.fastbiz.core.solution.descriptor.SolutionDescriptor;
import com.fastbiz.core.solution.ioc.BeanFactory;
import com.fastbiz.core.solution.ioc.DefaultBeanFactory;
import com.fastbiz.core.solution.spring.SolutionApplicationContext;

public class StandardSolution extends AbstractSolution{

    private AbstractApplicationContext applicationContext;

    private ApplicationContext         parent;

    private SolutionDescriptor         descriptor;

    private DefaultBeanFactory         beanFactory;

    public StandardSolution(SolutionDescriptor descriptor, ApplicationContext parent) {
        this.parent = parent;
        this.descriptor = descriptor;
        refresh();
    }

    @Override
    public void refresh(){
        close();
        if (applicationContext == null) {
            createApplicationContext();
        } else {
            applicationContext.refresh();
        }
    }

    protected void createApplicationContext(){
        applicationContext = new SolutionApplicationContext(descriptor, parent);
        beanFactory = new DefaultBeanFactory(applicationContext.getAutowireCapableBeanFactory());
    }

    @Override
    public void close(){
        if (applicationContext != null) {
            applicationContext.close();
        }
    }

    @Override
    public SolutionDescriptor getDescriptor(){
        return descriptor;
    }

    @Override
    protected BeanFactory getBeanFactory(){
        return beanFactory;
    }

    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> type){
        if (type == ApplicationContext.class) {
            return (T) applicationContext;
        }
        if (type == ConfigurableListableBeanFactory.class) {
            return (T) applicationContext.getBeanFactory();
        }
        if (type == AutowireCapableBeanFactory.class) {
            return (T) applicationContext.getAutowireCapableBeanFactory();
        }
        return null;
    }
}

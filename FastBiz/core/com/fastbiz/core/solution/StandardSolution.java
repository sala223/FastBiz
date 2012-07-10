package com.fastbiz.core.solution;

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
        return new DefaultBeanFactory(applicationContext);
    }

    @SuppressWarnings("unchecked")
    public <T> T unwrap(Class<T> type){
        if (type == ApplicationContext.class) {
            return (T) applicationContext;
        }
        return null;
    }
}

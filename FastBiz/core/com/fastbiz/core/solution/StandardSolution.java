package com.fastbiz.core.solution;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import com.fastbiz.core.solution.descriptor.SolutionDescriptor;
import com.fastbiz.core.solution.ioc.BeanContainer;
import com.fastbiz.core.solution.ioc.SpringBeanFactoryAdapter;
import com.fastbiz.core.solution.spring.SolutionApplicationContext;

public class StandardSolution extends AbstractSolution{

    private AbstractApplicationContext applicationContext;

    private ApplicationContext         parent;

    private SolutionDescriptor         descriptor;

    private SpringBeanFactoryAdapter   beanFactory;

    public StandardSolution(SolutionDescriptor descriptor, ApplicationContext parent) {
        this.parent = parent;
        this.descriptor = descriptor;
        refresh();
    }

    protected void refresh(){
        close();
        if (applicationContext == null) {
            createApplicationContext();
        } else {
            applicationContext.refresh();
        }
    }

    protected void createApplicationContext(){
        applicationContext = new SolutionApplicationContext(descriptor, parent);
        beanFactory = new SpringBeanFactoryAdapter(applicationContext.getAutowireCapableBeanFactory());
    }

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
    protected BeanContainer getBeanContainer(){
        return beanFactory;
    }

    @Override
    public ApplicationContext getApplicationContext(){
        return applicationContext;
    }
}

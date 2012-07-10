package com.fastbiz.core.solution.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import com.fastbiz.core.solution.SolutionDescriptorAware;
import com.fastbiz.core.solution.descriptor.SolutionDescriptor;

public class SolutionDescriptorAwareBeanPostProcessor implements BeanPostProcessor{

    private SolutionDescriptor solutionDescriptor;

    public SolutionDescriptorAwareBeanPostProcessor(SolutionDescriptor solutionDescriptor) {
        this.solutionDescriptor = solutionDescriptor;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException{
        if (bean instanceof SolutionDescriptorAware) {
            ((SolutionDescriptorAware) bean).setSolutionDescriptor(this.solutionDescriptor);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException{
        return bean;
    }
}

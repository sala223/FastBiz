package com.fastbiz.core.solution.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import com.fastbiz.core.solution.SolutionBrowser;
import com.fastbiz.core.solution.SolutionBrowserAware;

public class SolutionBrowserAwareBeanPostProcessor implements BeanPostProcessor{

    private SolutionBrowser solutionBrowser;

    public SolutionBrowserAwareBeanPostProcessor(SolutionBrowser solutionBrowser) {
        this.solutionBrowser = solutionBrowser;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException{
        if (bean instanceof SolutionBrowserAware) {
            ((SolutionBrowserAware) bean).setSolutionBrowser(this.solutionBrowser);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException{
        return bean;
    }
}

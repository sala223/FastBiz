package com.fastbiz.core.solution.ioc;

public interface BeanFactory{

    Object getBean(String beanName);

    Object getBean(String beanName, Object ... args);

    <T> T getBean(String beanName, Class<T> beanType);

    boolean containsBean(String name);

    <T> T getBean(Class<T> beanType);
}

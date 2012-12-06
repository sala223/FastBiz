package com.fastbiz.core.solution.ioc;

public interface BeanContainer{

    Object getBean(String beanName);

    Object getBean(String beanName, Object ... args);

    <T> T getBean(String beanName, Class<T> beanType);

    <T> T getBean(Class<T> beanType);

    boolean containsBean(String name);
}

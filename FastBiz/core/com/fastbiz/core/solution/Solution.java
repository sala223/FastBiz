package com.fastbiz.core.solution;

import org.springframework.context.ApplicationContext;
import com.fastbiz.core.solution.descriptor.SolutionDescriptor;
import com.fastbiz.core.solution.ioc.BeanContainer;

public interface Solution extends BeanContainer{

    SolutionDescriptor getDescriptor();

    ApplicationContext getApplicationContext();

    void close();
}

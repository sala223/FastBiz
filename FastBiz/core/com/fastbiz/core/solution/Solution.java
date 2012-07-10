package com.fastbiz.core.solution;

import com.fastbiz.core.solution.descriptor.SolutionDescriptor;
import com.fastbiz.core.solution.ioc.BeanFactory;

public interface Solution extends BeanFactory{

    void refresh();

    void close();

    SolutionDescriptor getDescriptor();
}

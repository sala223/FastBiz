package com.fastbiz.core.solution;

import com.fastbiz.core.solution.ioc.BeanFactory;

public interface SolutionFactory{

    Solution getSolution(String solutionId);

    BeanFactory getCoreBeanFactory();
    
}

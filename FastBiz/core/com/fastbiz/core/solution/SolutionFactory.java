package com.fastbiz.core.solution;

import com.fastbiz.core.solution.ioc.BeanContainer;

public interface SolutionFactory{

    Solution getSolution(String solutionId);

    BeanContainer getCoreBeanContainer();
}

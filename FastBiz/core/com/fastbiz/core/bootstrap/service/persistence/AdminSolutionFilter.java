package com.fastbiz.core.bootstrap.service.persistence;

import com.fastbiz.core.solution.descriptor.SolutionDescriptor;


public class AdminSolutionFilter implements SolutionFilter{

    @Override
    public boolean accept(SolutionDescriptor solution){
        return solution.isAdmin();
    }}

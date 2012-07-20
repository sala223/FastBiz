package com.fastbiz.core.bootstrap.service.persistence;

import com.fastbiz.core.solution.descriptor.SolutionDescriptor;

public interface SolutionFilter{

    boolean accept(SolutionDescriptor solution);
}

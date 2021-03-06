package com.fastbiz.core.testsuite;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.TestExecutionListeners;
import com.fastbiz.core.solution.Solution;

@RunWith(SolutionJunit4ClassRunner.class)
@TestExecutionListeners({ SolutionInjectionTestExecutionListener.class })
public abstract class SolutionTests implements SolutionAware{

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    protected Solution     solution;

    @Override
    public final void setSolution(Solution solution){
        this.solution = solution;
    }
}

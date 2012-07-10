package com.fastbiz.core.testsuite;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class SolutionJunit4ClassRunner extends SpringJUnit4ClassRunner{

    public SolutionJunit4ClassRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }
}

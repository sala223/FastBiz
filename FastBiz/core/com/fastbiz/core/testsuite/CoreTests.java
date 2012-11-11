package com.fastbiz.core.testsuite;

import org.junit.runner.RunWith;
import org.springframework.test.context.TestExecutionListeners;

@RunWith(SolutionJunit4ClassRunner.class)
@TestExecutionListeners({ CoreBeanInjectionTestExecutionListener.class })
public class CoreTests{}

package com.fastbiz.core.testsuite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import com.fastbiz.core.solution.SID;
import com.fastbiz.core.solution.Solution;
import com.fastbiz.core.solution.SolutionFactory;
import com.fastbiz.core.solution.StandardSolution;
import com.fastbiz.core.solution.StandardSolutionBrowser;
import com.fastbiz.core.tenant.TenantHolder;

public class SolutionInjectionTestExecutionListener extends AbstractTestExecutionListener{

    private static final Logger LOG                   = LoggerFactory.getLogger(SolutionInjectionTestExecutionListener.class);

    private static final String SOLUTION_FACTORY_ATTR = "SOLUTION_FACTORY";

    @Override
    public void prepareTestInstance(TestContext testContext) throws Exception{
        LOG.debug("Inject solution to TestClass {}", testContext.getTestInstance().getClass());
        injectDependencies(testContext);
        TenantHolder.setTenant("test");
    }

    protected void injectDependencies(TestContext testContext) throws Exception{
        Object testInstance = testContext.getTestInstance();
        if (testInstance instanceof SolutionAware) {
            Class<SID> annotationType = SID.class;
            Class<?> declaringClass = AnnotationUtils.findAnnotationDeclaringClass(annotationType, testContext.getTestClass());
            if (declaringClass == null) {
                String fmt = "@SID annotation must be configured for test class %s";
                throw new IllegalStateException(String.format(fmt, testContext.getTestClass()));
            }
            SID sid = declaringClass.getAnnotation(annotationType);
            String solutionId = sid.value();
            Solution solution = getSolution(testContext, solutionId);
            ((SolutionAware) testInstance).setSolution(solution);
            if (!(solution instanceof StandardSolution)) {
                throw new IllegalArgumentException("Not a standard solution, cannot upwrap AutowireCapableBeanFactory");
            }
            Object bean = testContext.getTestInstance();
            StandardSolution standardSolution = (StandardSolution) solution;
            AutowireCapableBeanFactory beanFactory = standardSolution.getApplicationContext().getAutowireCapableBeanFactory();
            beanFactory.autowireBeanProperties(bean, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
            beanFactory.initializeBean(bean, testContext.getTestClass().getName());
        }
    }

    protected Solution getSolution(TestContext testContext, String solutionId){
        Object obj = testContext.getAttribute(SOLUTION_FACTORY_ATTR);
        if (obj == null) {
            synchronized (testContext) {
                SolutionFactory factory = new EmbeddedSolutionFactory(new StandardSolutionBrowser());
                testContext.setAttribute(SOLUTION_FACTORY_ATTR, factory);
                obj = factory;
            }
        }
        return ((SolutionFactory) obj).getSolution(solutionId);
    }
}

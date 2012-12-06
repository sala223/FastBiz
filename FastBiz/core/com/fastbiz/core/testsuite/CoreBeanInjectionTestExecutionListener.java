package com.fastbiz.core.testsuite;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import com.fastbiz.core.solution.StandardSolutionBrowser;
import com.fastbiz.core.solution.ioc.SpringBeanFactoryAdapter;
import com.fastbiz.core.solution.spring.StandardSolutionFactory;
import com.fastbiz.core.tenant.TenantHolder;

public class CoreBeanInjectionTestExecutionListener extends AbstractTestExecutionListener{

    private static final String SOLUTION_FACTORY_ATTR = "SOLUTION_FACTORY";

    @Override
    public void prepareTestInstance(final TestContext testContext) throws Exception{
        injectDependencies(testContext);
        TenantHolder.setTenant("test");
    }

    @Override
    public void beforeTestMethod(final TestContext testContext) throws Exception{
        injectDependencies(testContext);
    }

    protected void injectDependencies(final TestContext testContext) throws Exception{
        Object bean = testContext.getTestInstance();
        StandardSolutionFactory obj = (StandardSolutionFactory) testContext.getAttribute(SOLUTION_FACTORY_ATTR);
        if (obj == null) {
            synchronized (testContext) {
                obj = new EmbeddedSolutionFactory(new StandardSolutionBrowser());
                testContext.setAttribute(SOLUTION_FACTORY_ATTR, obj);
            }
        }
        SpringBeanFactoryAdapter coreBeanFactory = (SpringBeanFactoryAdapter) obj.getCoreBeanContainer();
        AutowireCapableBeanFactory beanFactory = (AutowireCapableBeanFactory) coreBeanFactory.unwrap();
        beanFactory.autowireBeanProperties(bean, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
        beanFactory.initializeBean(bean, testContext.getTestClass().getName());
    }
}

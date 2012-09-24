package com.fastbiz.core.bootstrap.service.solution;

import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fastbiz.core.bootstrap.Application;
import com.fastbiz.core.bootstrap.service.BootstrapServiceBase;
import com.fastbiz.core.bootstrap.service.BootstrapServiceException;
import com.fastbiz.core.bootstrap.service.jndi.JNDI;
import com.fastbiz.core.solution.SolutionFactory;
import com.fastbiz.core.solution.StandardSolutionBrowser;
import com.fastbiz.core.solution.spring.StandardSolutionFactory;

public class SolutionPackageService extends BootstrapServiceBase{

    private static final Logger LOG                   = LoggerFactory.getLogger(SolutionPackageService.class);

    private SolutionFactory     factory;

    StandardSolutionBrowser     browser;

    public static final String  SOLUTION_FACTORY_JNDI = "java:/comp/SolutionFactory";

    public static final String  SOLUTION_BROWSER_JNDI = "java:/comp/SolutionBrowser";

    public void init(Application application){
        browser = new StandardSolutionBrowser();
    }

    public void start(Application application){
        factory = new StandardSolutionFactory(browser);
        String[] solutionIds = browser.getSolutionIds();
        for (String solutionId : solutionIds) {
            LOG.info("###Start to create solution context for solution {}###", solutionId);
            try {
                factory.getSolution(solutionId);
            } catch (Throwable ex) {
                LOG.error(String.format("Failed create solution context for solution %s", solutionId), ex);
            }
        }
        try {
            application.getBootstrapService(JNDI.class).bind(SOLUTION_FACTORY_JNDI, factory);
            LOG.info("Bind SolutionFactory to {}", SOLUTION_FACTORY_JNDI);
        } catch (NamingException ex) {
            String fmt = "Cannot bind solution factory instance to JNDI %s";
            throw new BootstrapServiceException(this.getClass().getName(), ex, fmt, SOLUTION_FACTORY_JNDI);
        }
        try {
            application.getBootstrapService(JNDI.class).bind(SOLUTION_BROWSER_JNDI, browser);
            LOG.info("Bind SolutionBroser to {}", SOLUTION_BROWSER_JNDI);
        } catch (NamingException ex) {
            String fmt = "Cannot bind solution browser instance to JNDI %s";
            throw new BootstrapServiceException(this.getClass().getName(), ex, fmt, SOLUTION_BROWSER_JNDI);
        }
    }

    public void stop(Application application){
        if (factory != null) {
            ((StandardSolutionFactory) factory).close();
        }
    }
}

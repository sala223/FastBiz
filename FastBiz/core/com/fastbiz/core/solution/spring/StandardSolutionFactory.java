package com.fastbiz.core.solution.spring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.fastbiz.common.utils.DebugUtils;
import com.fastbiz.core.solution.Solution;
import com.fastbiz.core.solution.SolutionBrowser;
import com.fastbiz.core.solution.SolutionBrowserAware;
import com.fastbiz.core.solution.SolutionException;
import com.fastbiz.core.solution.SolutionFactory;
import com.fastbiz.core.solution.StandardSolution;
import com.fastbiz.core.solution.ioc.BeanContainer;
import com.fastbiz.core.solution.ioc.SpringBeanFactoryAdapter;

public class StandardSolutionFactory implements SolutionFactory{

    protected SolutionBrowser     browser;

    private Map<String, Solution> solutions               = new HashMap<String, Solution>();

    private Lock                  lock                    = new ReentrantLock();

    private ApplicationContext    parent;

    private static final String   PARENT_BEAN_CONFIG_FILE = "core-beans.xml";

    public StandardSolutionFactory(SolutionBrowser solutionBrowser, boolean lazy) {
        this.browser = solutionBrowser;
        if (!lazy) {
            createParentApplicationContext();
        }
    }

    public StandardSolutionFactory(SolutionBrowser solutionBrowser) {
        this(solutionBrowser, false);
    }

    @Override
    public Solution getSolution(String solutionId){
        if (parent == null) {
            lock.lock();
            try {
                if (parent == null) {
                    createParentApplicationContext();
                }
            } finally {
                lock.unlock();
            }
        }
        Solution solution = solutions.get(solutionId);
        if (solution == null) {
            if (browser.hasSolution(solutionId)) {
                try {
                    lock.lock();
                    if (solutionId.contains(solutionId)) {
                        solution = createNewSolution(solutionId);
                        if (solution != null) {
                            solutions.put(solutionId, solution);
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
        if (solution == null) {
            throw SolutionException.SolutionDoesNotExist(solutionId);
        }
        return solution;
    }

    protected Solution createNewSolution(String solutionId){
        return new StandardSolution(browser.getSolutionDescriptor(solutionId), parent);
    }

    protected void createParentApplicationContext(){
        parent = new SolutionParentApplicationContext(browser, new String[] { PARENT_BEAN_CONFIG_FILE });
    }

    public void close(){
        for (Solution solution : solutions.values()) {
            solution.close();
        }
        solutions.clear();
    }

    private static final class SolutionParentApplicationContext extends ClassPathXmlApplicationContext{

        private SolutionBrowser     solutionBrowser;

        private static String       SOLUTION_BROWSER_BEAN_NAME = "SOLUTION_BROWSER";

        private static final Logger LOG                        = LoggerFactory.getLogger(SolutionParentApplicationContext.class);

        public SolutionParentApplicationContext(SolutionBrowser solutionBrowser, String[] configLocations) {
            this.solutionBrowser = solutionBrowser;
            setConfigLocations(configLocations);
            setProfiles();
            refresh();
        }

        private void setProfiles(){
            List<String> profiles = new ArrayList<String>();
            if (DebugUtils.isDebugEnabled()) {
                LOG.debug("Enable debug profile.");
                profiles.add("debug");
            }
            if (Boolean.parseBoolean(System.getProperty("ENTITY_AUTO_WIRE"))) {
                LOG.debug("Enable autowire profile.");
                profiles.add("autowire");
            }
            if (profiles.size() > 0) {
                this.getEnvironment().setActiveProfiles(profiles.toArray(new String[0]));
            }
        }

        @Override
        protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory){
            super.postProcessBeanFactory(beanFactory);
            beanFactory.addBeanPostProcessor(new SolutionBrowserAwareBeanPostProcessor(solutionBrowser));
            beanFactory.ignoreDependencyInterface(SolutionBrowserAware.class);
            if (!beanFactory.containsBean(SOLUTION_BROWSER_BEAN_NAME)) {
                beanFactory.registerSingleton(SOLUTION_BROWSER_BEAN_NAME, solutionBrowser);
            }
        }
    }

    @Override
    public BeanContainer getCoreBeanContainer(){
        if (parent == null) {
            synchronized (this) {
                this.createParentApplicationContext();
            }
        }
        return new SpringBeanFactoryAdapter(parent.getAutowireCapableBeanFactory());
    }
}

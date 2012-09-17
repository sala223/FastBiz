package com.fastbiz.core.solution.spring;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import com.fastbiz.core.solution.SolutionDescriptorAware;
import com.fastbiz.core.solution.descriptor.SolutionDescriptor;

public class SolutionApplicationContext extends AbstractXmlApplicationContext{

    private SolutionDescriptor  solutionDescriptor;

    public static String        SOLUTION_DESCRIPTOR_BEAN_NAME = "SOLUTION_DESCRIPTOR";

    private static final String DEFAULT_BEAN_CONFIG_FILE      = "classpath:tpl.xml";

    public SolutionApplicationContext(SolutionDescriptor descriptor, ApplicationContext parent, boolean refresh) {
        super(parent);
        this.solutionDescriptor = descriptor;
        String[] confPathes = descriptor.getBeanConfigurationFilesPathes();
        String[] newConfPathes = new String[confPathes.length + 1];
        newConfPathes[0] = DEFAULT_BEAN_CONFIG_FILE;
        System.arraycopy(confPathes, 0, newConfPathes, 1, confPathes.length);
        setConfigLocations(newConfPathes);
        if (refresh) {
            refresh();
        }
    }

    public SolutionApplicationContext(SolutionDescriptor descriptor, ApplicationContext parent) {
        this(descriptor, parent, true);
    }

    public SolutionApplicationContext(SolutionDescriptor descriptor) {
        this(descriptor, null, true);
    }

    @Override
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory){
        super.postProcessBeanFactory(beanFactory);
        beanFactory.addBeanPostProcessor(new SolutionDescriptorAwareBeanPostProcessor(solutionDescriptor));
        beanFactory.ignoreDependencyInterface(SolutionDescriptorAware.class);
        if (!beanFactory.containsBean(SOLUTION_DESCRIPTOR_BEAN_NAME)) {
            beanFactory.registerSingleton(SOLUTION_DESCRIPTOR_BEAN_NAME, solutionDescriptor);
        }
    }

    @Override
    protected Resource getResourceByPath(String path){
        if (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        return new FileSystemResource(path);
    }
}

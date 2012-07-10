package com.fastbiz.core.testsuite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.fastbiz.core.bootstrap.Application;
import com.fastbiz.core.bootstrap.service.BootstrapService;
import com.fastbiz.core.bootstrap.service.BootstrapServiceException;
import com.fastbiz.core.bootstrap.service.datasource.DataSourceService;
import com.fastbiz.core.bootstrap.service.derby.Derby;
import com.fastbiz.core.bootstrap.service.jndi.JNDI;
import com.fastbiz.core.solution.SolutionBrowser;
import com.fastbiz.core.solution.spring.StandardSolutionFactory;

public class EmbeddedSolutionFactory extends StandardSolutionFactory{

    private List<BootstrapService> services;

    public EmbeddedSolutionFactory(SolutionBrowser solutionBrowser) {
        super(solutionBrowser,true);
        services = new ArrayList<BootstrapService>();
        startBootstrapService(Derby.class);
        startBootstrapService(JNDI.class);
        startBootstrapService(DataSourceService.class);
    }

    protected void startBootstrapService(Class<? extends BootstrapService> serviceType){
        try {
            BootstrapService service = serviceType.newInstance();
            service.init(Application.getApplication());
            service.start(Application.getApplication());
            services.add(service);
        } catch (InstantiationException ex) {
            throw new BootstrapServiceException(serviceType.getName(), ex);
        } catch (IllegalAccessException ex) {
            throw new BootstrapServiceException(serviceType.getName(), ex);
        }
    }

    protected void shutdownBootstrapServices(){
        Collections.reverse(services);
        for (BootstrapService service : services) {
            service.stop(Application.getApplication());
        }
        services.clear();
    }

    @Override
    public void close(){
        super.close();
        shutdownBootstrapServices();
    }
}

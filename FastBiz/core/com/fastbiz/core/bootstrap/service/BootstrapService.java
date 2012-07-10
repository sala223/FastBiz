package com.fastbiz.core.bootstrap.service;

import java.util.Iterator;
import com.fastbiz.core.bootstrap.Application;

public interface BootstrapService{

    void init(Application application) throws BootstrapServiceException;

    void start(Application application) throws BootstrapServiceException;

    void stop(Application application) throws BootstrapServiceException;

    void addServiceEventListener(ServiceEventListener listener);

    Iterator<ServiceEventListener> getServiceEventListeners();

    void fireServiceEvent(BootstrapServiceEvent event);
}

package com.fastbiz.core.bootstrap.service;

import java.util.Iterator;

public abstract class BootstrapServiceBase implements BootstrapService{

    protected BootstrapServiceSupport support = new BootstrapServiceSupport(this);

    @Override
    public void addServiceEventListener(ServiceEventListener listener){
        support.addServiceEventListener(listener);
    }

    @Override
    public Iterator<ServiceEventListener> getServiceEventListeners(){
        return support.getServiceEventListeners();
    }

    @Override
    public void fireServiceEvent(BootstrapServiceEvent event){
        support.fireServiceEvent(event.getLifecycle(), event.getLevel(), event.getData());
    }
}

package com.fastbiz.core.bootstrap.service;

import com.fastbiz.core.bootstrap.service.BootstrapServiceEvent.Lifecycle;

public class ServiceEventListenerAdapter implements ServiceEventListener{

    public void onEvent(BootstrapServiceEvent event){
        if (event != null) {
            Lifecycle lifecycle = event.getLifecycle();
            switch (lifecycle) {
            case init:
                onInit(event);
                break;
            case start:
                onStart(event);
                break;
            case stop:
                onStop(event);
                break;
            case running:
                onRunning(event);
                break;
            default:
                break;
            }
        }
    }

    protected void onInit(BootstrapServiceEvent event){}

    protected void onStart(BootstrapServiceEvent event){}

    protected void onStop(BootstrapServiceEvent event){}

    protected void onRunning(BootstrapServiceEvent event){}
}

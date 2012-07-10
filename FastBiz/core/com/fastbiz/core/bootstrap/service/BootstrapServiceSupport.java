package com.fastbiz.core.bootstrap.service;

import java.util.Iterator;
import java.util.Vector;
import org.springframework.util.Assert;
import com.fastbiz.core.bootstrap.service.BootstrapServiceEvent.Level;
import com.fastbiz.core.bootstrap.service.BootstrapServiceEvent.Lifecycle;

public final class BootstrapServiceSupport{

    private BootstrapService             service;

    private Vector<ServiceEventListener> listeners = new Vector<ServiceEventListener>();

    public BootstrapServiceSupport(BootstrapService service) {
        Assert.notNull(service);
        this.service = service;
    }

    public void addServiceEventListener(ServiceEventListener listener){
        listeners.add(listener);
    }

    public Iterator<ServiceEventListener> getServiceEventListeners(){
        return listeners.iterator();
    }

    public void fireServiceEvent(Lifecycle lifecycle, Level level, Object data){
        BootstrapServiceEvent event = new BootstrapServiceEvent(service, lifecycle, level, data);
        for (ServiceEventListener listener : listeners)
            listener.onEvent(event);
    }

    public void removeServiceEventListener(ServiceEventListener listener){
        listeners.remove(listener);
    }
}

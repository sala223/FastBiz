package com.fastbiz.core.bootstrap.service;

import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fastbiz.common.utils.Assert;
import com.fastbiz.core.bootstrap.Application;
import com.fastbiz.core.bootstrap.service.BootstrapServiceEvent.Level;
import com.fastbiz.core.bootstrap.service.BootstrapServiceEvent.Lifecycle;

public class BootstrapServiceWrapper implements BootstrapService{

    private static final Logger LOG = LoggerFactory.getLogger(BootstrapServiceWrapper.class);

    protected BootstrapService  service;

    public BootstrapServiceWrapper(BootstrapService service) {
        Assert.notNull(service);
        this.service = service;
    }

    @Override
    public void init(Application application){
        try {
            LOG.info("Start to initialize bootstrap service {} ", service.getClass());
            service.init(application);
            Iterator<ServiceEventListener> iterator = service.getServiceEventListeners();
            while (iterator != null && iterator.hasNext()) {
                ServiceEventListener listener = iterator.next();
                try {
                    listener.onEvent(new BootstrapServiceEvent(service, Lifecycle.init, Level.info));
                } catch (Throwable e) {
                    String fmt = new String("Failed executing listener %s on initializing bootstrap service %s");
                    LOG.error(String.format(fmt, listener.getClass(), this.service), e);
                }
            }
        } catch (Throwable e) {
            fireServiceEvent(BootstrapServiceEvent.createInitErrorEvent(service, e));
        }
    }

    @Override
    public void start(Application application){
        try {
            LOG.info("Start to run bootstrap service {} ", service.getClass());
            service.start(Application.getApplication());
            Iterator<ServiceEventListener> iterator = service.getServiceEventListeners();
            while (iterator != null && iterator.hasNext()) {
                ServiceEventListener listener = iterator.next();
                try {
                    listener.onEvent(new BootstrapServiceEvent(service, Lifecycle.start, Level.info));
                } catch (Throwable e) {
                    String fmt = new String("Failed executing listener %s on starting bootstrap service %s");
                    LOG.error(String.format(fmt, listener.getClass(), this.service), e);
                }
            }
        } catch (Throwable ex) {
            fireServiceEvent(BootstrapServiceEvent.createStartErrorEvent(service, ex));
        }
    }

    @Override
    public void stop(Application application){
        try {
            LOG.info("Start to stop service {} ", service.getClass());
            service.stop(Application.getApplication());
            Iterator<ServiceEventListener> iterator = service.getServiceEventListeners();
            while (iterator != null && iterator.hasNext()) {
                ServiceEventListener listener = iterator.next();
                try {
                    listener.onEvent(new BootstrapServiceEvent(service, Lifecycle.start, Level.info));
                } catch (Throwable e) {
                    String fmt = new String("Failed executing listener %s on stopping bootstrap service %s");
                    LOG.error(String.format(fmt, listener.getClass(), this.service), e);
                }
            }
        } catch (Throwable e) {
            fireServiceEvent(BootstrapServiceEvent.createStopErrorEvent(service, e));
        }
    }

    @Override
    public void addServiceEventListener(ServiceEventListener listener){
        service.addServiceEventListener(listener);
    }

    @Override
    public Iterator<ServiceEventListener> getServiceEventListeners(){
        return service.getServiceEventListeners();
    }

    @Override
    public void fireServiceEvent(BootstrapServiceEvent event){
        service.fireServiceEvent(event);
    }

    public BootstrapService unwrap(){
        return service;
    }
}

package com.fastbiz.core.bootstrap.service;

import java.util.EventObject;

public class BootstrapServiceEvent extends EventObject{

    private static final long serialVersionUID = 1L;

    private Object            data;

    private Lifecycle         lifecycle;

    private Level             level;

    public static enum Lifecycle {
        init, start, stop, running
    }

    public static enum Level {
        info, warning, error,
    }

    public BootstrapServiceEvent(BootstrapService source, Lifecycle phase, Level level, Object data) {
        super(source);
        setLifecycle(phase);
        setLevel(level);
        setData(data);
    }

    public BootstrapServiceEvent(BootstrapService source, Lifecycle phase, Level level) {
        this(source, phase, level, null);
    }

    public Object getData(){
        return data;
    }

    public void setData(Object data){
        this.data = data;
    }

    public Lifecycle getLifecycle(){
        return lifecycle;
    }

    public void setLifecycle(Lifecycle lifecycle){
        this.lifecycle = lifecycle;
    }

    public Level getLevel(){
        return level;
    }

    public void setLevel(Level level){
        this.level = level;
    }

    public static BootstrapServiceEvent createInitErrorEvent(BootstrapService service, Object data){
        return new BootstrapServiceEvent(service, Lifecycle.init, Level.error, data);
    }

    public static BootstrapServiceEvent createStartErrorEvent(BootstrapService service, Object data){
        return new BootstrapServiceEvent(service, Lifecycle.start, Level.error, data);
    }

    public static BootstrapServiceEvent createStopErrorEvent(BootstrapService service, Object data){
        return new BootstrapServiceEvent(service, Lifecycle.stop, Level.error, data);
    }

    public static BootstrapServiceEvent createInitInfoEvent(BootstrapService service, Object data){
        return new BootstrapServiceEvent(service, Lifecycle.init, Level.info, data);
    }

    public static BootstrapServiceEvent createStartInfoEvent(BootstrapService service, Object data){
        return new BootstrapServiceEvent(service, Lifecycle.start, Level.info, data);
    }

    public static BootstrapServiceEvent createStopInfoEvent(BootstrapService service, Object data){
        return new BootstrapServiceEvent(service, Lifecycle.stop, Level.info, data);
    }

    public static BootstrapServiceEvent createInitWarnEvent(BootstrapService service, Object data){
        return new BootstrapServiceEvent(service, Lifecycle.init, Level.warning, data);
    }

    public static BootstrapServiceEvent createStartWarningEvent(BootstrapService service, Object data){
        return new BootstrapServiceEvent(service, Lifecycle.start, Level.warning, data);
    }

    public static BootstrapServiceEvent createStopWarningEvent(BootstrapService service, Object data){
        return new BootstrapServiceEvent(service, Lifecycle.stop, Level.warning, data);
    }
}

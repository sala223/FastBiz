package com.fastbiz.core.bootstrap.service.appserver;

public interface Embedded{

    public void init(EmbeddedSpec spec);

    public void addApplicationContext(ApplicationDescriptor application);

    public void start();

    public void stop();
}

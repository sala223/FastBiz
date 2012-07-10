package com.fastbiz.core.bootstrap.service.appserver;

public interface EmbeddedSpec{

    int getHttpPort();

    int getHttpsPort();

    String getStartupClassName();
}

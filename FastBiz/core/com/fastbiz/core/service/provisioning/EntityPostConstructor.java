package com.fastbiz.core.service.provisioning;

public interface EntityPostConstructor{

    void processEntity(Object obj);

    boolean supportEntity(Object obj);
}

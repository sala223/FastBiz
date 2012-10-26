package com.fastbiz.core.entity.metadata;

public interface EntityAttrDescriptor{

    String getName();

    Object accessValue(Object entity);
}

package com.fastbiz.core.entity.validation;

import com.fastbiz.core.entity.metadata.EntityAttrDescriptor;

public class CheckInvokerContext{

    private Object               owner;

    private EntityAttrDescriptor descriptor;

    private Object               attributeValue;

    public CheckInvokerContext(Object owner, EntityAttrDescriptor descriptor, Object attributeValue) {
        this.owner = owner;
        this.descriptor = descriptor;
        this.attributeValue = attributeValue;
    }

    public Object getOwner(){
        return owner;
    }

    public EntityAttrDescriptor getAttributeDescriptor(){
        return descriptor;
    }

    public Object getAttributeValue(){
        return attributeValue;
    }
}

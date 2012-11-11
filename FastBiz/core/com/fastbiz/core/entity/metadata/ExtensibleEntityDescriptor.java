package com.fastbiz.core.entity.metadata;

import java.util.List;

public class ExtensibleEntityDescriptor{

    private String                             entity;

    private List<EntityExtendedAttrDescriptor> extendedAttributes;

    public String getEntity(){
        return entity;
    }

    public void setEntity(String entity){
        this.entity = entity;
    }

    public List<EntityExtendedAttrDescriptor> getExtendedAttributes(){
        return extendedAttributes;
    }

    public void setExtendedAttributes(List<EntityExtendedAttrDescriptor> extendedAttributes){
        this.extendedAttributes = extendedAttributes;
    }
}

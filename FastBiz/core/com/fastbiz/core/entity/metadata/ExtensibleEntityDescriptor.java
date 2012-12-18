package com.fastbiz.core.entity.metadata;

import java.util.List;

public class ExtensibleEntityDescriptor{

    private String                          entity;

    private List<ExtensibleEntityAttrGroup> extendedAttrGroups;

    public String getEntity(){
        return entity;
    }

    public void setEntity(String entity){
        this.entity = entity;
    }

    public List<ExtensibleEntityAttrGroup> getExtendedAttrGroups(){
        return extendedAttrGroups;
    }

    public void setExtendedAttrGroups(List<ExtensibleEntityAttrGroup> extendedAttrGroups){
        this.extendedAttrGroups = extendedAttrGroups;
    }
}

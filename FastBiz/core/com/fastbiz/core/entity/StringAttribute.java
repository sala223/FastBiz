package com.fastbiz.core.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StringAttribute implements Attribute{

    @Column(name = "attribute", length = 56)
    private String attributeName;

    @Column(name = "value", length = 1024)
    private String value;

    public StringAttribute() {}

    public StringAttribute(String attributeName, String value) {
        this.setAttributeName(attributeName);
        this.setValue(value);
    }

    public String getAttributeName(){
        return attributeName;
    }

    public void setAttributeName(String attributeName){
        this.attributeName = attributeName;
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }
}

package com.fastbiz.core.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DecimalAttribute implements Attribute{

    @Column(name = "attribute", length = 56)
    private String attributeName;

    @Column(name = "value", precision = 12, scale = 4)
    private Number value;

    public DecimalAttribute() {}

    public DecimalAttribute(String attributeName, Number value) {
        this.setAttributeName(attributeName);
        this.setValue(value);
    }

    public String getAttributeName(){
        return attributeName;
    }

    public void setAttributeName(String attributeName){
        this.attributeName = attributeName;
    }

    public Number getValue(){
        return value;
    }

    public void setValue(Number value){
        this.value = value;
    }
}

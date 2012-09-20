package com.fastbiz.core.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class DateTimeAttribute implements Attribute{

    @Column(name = "attribute", length = 56)
    private String attributeName;

    @Column(name = "value")
    @Temporal(value = TemporalType.TIME)
    private Date   value;

    public DateTimeAttribute() {}

    public DateTimeAttribute(String attributeName, Date value) {
        this.setAttributeName(attributeName);
        this.setValue(value);
    }

    public String getAttributeName(){
        return attributeName;
    }

    public void setAttributeName(String attributeName){
        this.attributeName = attributeName;
    }

    public Date getValue(){
        return value;
    }

    public void setValue(Date value){
        this.value = value;
    }
}

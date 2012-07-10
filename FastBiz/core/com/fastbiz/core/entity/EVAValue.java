package com.fastbiz.core.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class EVAValue implements Serializable{

    private static final long serialVersionUID = 1L;
    @Column(name = "EVA_VALUE", length = 511)
    private String            value;

    public EVAValue() {}

    public EVAValue(String value) {
        setValue(value);
    }

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }
}

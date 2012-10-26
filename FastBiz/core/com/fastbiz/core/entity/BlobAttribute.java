package com.fastbiz.core.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.Lob;

@Embeddable
public class BlobAttribute implements Attribute{

    @Column(name = "attribute", length = 56)
    private String attributeName;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "value")
    private byte[] value;

    public BlobAttribute() {}

    public BlobAttribute(String attributeName, byte[] value) {
        this.setAttributeName(attributeName);
        this.setValue(value);
    }

    public String getAttributeName(){
        return attributeName;
    }

    public void setAttributeName(String attributeName){
        this.attributeName = attributeName;
    }

    public byte[] getValue(){
        return value;
    }

    public void setValue(byte[] value){
        this.value = value;
    }
}

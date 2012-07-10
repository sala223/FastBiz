package com.fastbiz.core.entity.type;

import java.io.Serializable;
import javax.persistence.Embeddable;

@Embeddable
public class Image implements Serializable{

    private static final long serialVersionUID = 1L;
    private byte[]            binary;

    public byte[] getBinary(){
        return binary;
    }

    public void setBinary(byte[] binary){
        this.binary = binary;
    }
}

package com.fastbiz.common.security;

import java.io.Serializable;
import java.util.Arrays;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class KeyInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    private String            algorithm;

    private String            alias;

    private char[]            password;

    public String getAlgorithm(){
        return algorithm;
    }

    public void setAlgorithm(String algorithm){
        this.algorithm = algorithm;
    }

    public String getAlias(){
        return alias;
    }

    public void setAlias(String alias){
        this.alias = alias;
    }

    public char[] getPassword(){
        return password;
    }

    public void setPassword(char[] password){
        this.password = password;
    }

    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ algorithm=" + algorithm);
        buffer.append(", alias=" + alias);
        buffer.append(", password=" + Arrays.toString(password) + " }");
        return buffer.toString();
    }
}

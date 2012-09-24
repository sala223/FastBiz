package com.fastbiz.common.security;

import java.io.Serializable;
import java.util.Arrays;
import javax.xml.bind.annotation.XmlRootElement;
import com.fastbiz.common.utils.SecurityUtils;

@XmlRootElement
public class KeyStoreInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    private String            type;

    private String            filePath;

    private char[]            password;

    public KeyStoreInfo() {
        this.type = SecurityUtils.KEY_STORE_TYPE_JKS;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getFilePath(){
        return filePath;
    }

    public void setFilePath(String filePath){
        this.filePath = filePath;
    }

    public char[] getPassword(){
        return password;
    }

    public void setPassword(char[] password){
        this.password = password;
    }

    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("Type=" + type);
        buffer.append(",filePath=" + filePath);
        buffer.append(",password=" + Arrays.toString(password));
        return buffer.toString();
    }
}

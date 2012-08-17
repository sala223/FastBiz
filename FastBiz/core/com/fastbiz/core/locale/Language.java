package com.fastbiz.core.locale;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Language{

    private String code;

    private String display;

    public Language() {}

    public Language(String code, String display) {
        this.code = code;
        this.display = display;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getDisplay(){
        return display;
    }

    public void setDisplay(String display){
        this.display = display;
    }
}

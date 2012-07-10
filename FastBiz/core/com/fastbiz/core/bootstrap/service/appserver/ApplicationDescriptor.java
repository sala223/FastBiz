package com.fastbiz.core.bootstrap.service.appserver;

import java.io.Serializable;

public class ApplicationDescriptor implements Serializable{

    private static final long serialVersionUID = 1L;

    private String            path;

    private String            docBase;

    private String            name;

    private ApplicationType   type;

    ApplicationDescriptor() {}

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getPath(){
        return path;
    }

    public void setPath(String path){
        this.path = path;
    }

    public String getDocBase(){
        return docBase;
    }

    public void setDocBase(String docBase){
        this.docBase = docBase;
    }

    public ApplicationType getType(){
        return type;
    }

    public void setType(ApplicationType type){
        this.type = type;
    }

    public static enum ApplicationType {
        STADARD_WAR, STANDARD_FOLDER
    }
}

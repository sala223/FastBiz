package com.fastbiz.core.dal.view;

import java.io.Serializable;

public class ViewId implements Serializable{

    private static final long serialVersionUID = 1L;

    private String            viewName;

    private String            entityName;

    public ViewId(String entityName, String name) {
        this.entityName = entityName;
        this.viewName = name;
    }

    public String getViewName(){
        return viewName;
    }

    public void setViewName(String name){
        this.viewName = name;
    }

    public String getEntityName(){
        return entityName;
    }

    public void setEntityName(String entityName){
        this.entityName = entityName;
    }
}

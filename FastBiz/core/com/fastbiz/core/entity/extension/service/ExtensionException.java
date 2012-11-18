package com.fastbiz.core.entity.extension.service;

import com.fastbiz.core.biz.exception.BusinessException;

public class ExtensionException extends BusinessException{

    private static final long serialVersionUID = 1L;

    public ExtensionException(int errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ExtensionException(int errorCode, String message, Object ... args) {
        super(errorCode, message, args);
    }

    public static ExtensionException noSuchEntityException(String entity){
        return new ExtensionException(1001, "Entity %s does not exist", entity);
    }
    
    public static ExtensionException notAnEntityClassException(Class<?> entityClass){
        return new ExtensionException(1002, "Class %s is not an entity", entityClass.getName());
    }

    public static ExtensionException EntityAttributeAlreadyExistException(String entityName, String attr){
        return new ExtensionException(1003, "Entity %s Attribute %s already exist", entityName, attr);
    }
}

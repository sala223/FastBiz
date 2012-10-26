package com.fastbiz.core.service.extension;

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
}

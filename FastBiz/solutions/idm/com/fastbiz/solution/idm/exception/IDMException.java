package com.fastbiz.solution.idm.exception;

import com.fastbiz.core.biz.exception.ServiceException;

public class IDMException extends ServiceException{

    private static final long serialVersionUID = 1L;

    public IDMException(String serviceName, int errorCode) {
        super(serviceName, errorCode);
    }

    public IDMException(String serviceName, int errorCode, Throwable cause) {
        super(serviceName, errorCode, cause);
    }

    public IDMException(String serviceName, int errorCode, String message, Object ... args) {
        super(serviceName, errorCode, message, args);
    }
}

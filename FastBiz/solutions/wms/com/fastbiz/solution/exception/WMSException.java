package com.fastbiz.solution.exception;

import com.fastbiz.core.biz.exception.ServiceException;

public class WMSException extends ServiceException{

    private static final long serialVersionUID = 1L;

    public WMSException(String serviceName, String errorCode) {
        super(serviceName, errorCode);
    }

    public WMSException(String serviceName, String errorCode, Throwable cause) {
        super(serviceName, errorCode, cause);
    }

    public WMSException(String serviceName, String errorCode, String message, Object ... args) {
        super(serviceName, errorCode, message, args);
    }
}

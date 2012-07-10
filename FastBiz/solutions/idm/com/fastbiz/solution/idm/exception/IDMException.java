package com.fastbiz.solution.idm.exception;

import com.fastbiz.core.biz.exception.ServiceException;

public class IDMException extends ServiceException{

    private static final long   serialVersionUID = 1L;

    private static final String IDM_SOLUTION_ID  = "idm";

    public IDMException(String serviceName, int errorCode) {
        super(IDM_SOLUTION_ID, serviceName, errorCode);
    }

    public IDMException(String serviceName, int errorCode, Throwable cause) {
        super(IDM_SOLUTION_ID, serviceName, errorCode, cause);
    }

    public IDMException(String serviceName, int errorCode, String message, Object ... args) {
        super(IDM_SOLUTION_ID, serviceName, errorCode, message, args);
    }
}

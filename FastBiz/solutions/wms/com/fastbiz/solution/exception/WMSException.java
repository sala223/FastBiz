package com.fastbiz.solution.exception;

import com.fastbiz.core.biz.exception.ServiceException;

public class WMSException extends ServiceException{

    private static final long   serialVersionUID = 1L;

    private static final String WMS_SOLUTION_ID  = "wms";

    public WMSException(String serviceName, int errorCode) {
        super(WMS_SOLUTION_ID, serviceName, errorCode);
    }

    public WMSException(String serviceName, int errorCode, Throwable cause) {
        super(WMS_SOLUTION_ID, serviceName, errorCode, cause);
    }

    public WMSException(String serviceName, int errorCode, String message, Object ... args) {
        super(WMS_SOLUTION_ID, serviceName, errorCode, message, args);
    }
}

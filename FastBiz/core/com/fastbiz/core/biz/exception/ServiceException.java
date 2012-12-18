package com.fastbiz.core.biz.exception;

import java.io.StringWriter;

public class ServiceException extends BusinessException{

    private static final long serialVersionUID = 1L;

    protected String          serviceName;

    public ServiceException(String serviceName, String errorCode) {
        this(serviceName, errorCode, null);
    }

    public ServiceException(String serviceName, String errorCode, Throwable cause) {
        super(errorCode, cause);
        this.serviceName = serviceName;
    }

    public ServiceException(String serviceName, String errorCode, String message, Object ... args) {
        super(errorCode, message, args);
        this.serviceName = serviceName;
    }

    public String getServiceName(){
        return serviceName;
    }

    @Override
    public String getMessage(){
        StringWriter writer = new StringWriter(100);
        writer.write(CR);
        writer.write(indentation);
        writer.write("Service Name:" + getServiceName());
        writer.write(CR);
        writer.write(indentation);
        writer.write("Error Code:" + getErrorCode());
        writer.write(CR);
        writer.write(indentation);
        writer.write("Detail Internal Message:" + getInternalMessage());
        Throwable cause = this.getCause();
        if (cause != null && (cause instanceof java.lang.reflect.InvocationTargetException)) {
            writer.write(CR);
            writer.write(indentation);
            writer.write("Target Invocation Exception:");
            Throwable target = ((java.lang.reflect.InvocationTargetException) cause).getTargetException();
            if (target != null) {
                writer.write(target.getMessage());
            }
        }
        return writer.toString();
    }
}

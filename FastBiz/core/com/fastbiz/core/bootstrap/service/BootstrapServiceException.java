package com.fastbiz.core.bootstrap.service;

import java.io.StringWriter;
import com.fastbiz.common.exception.InfrastructureException;

public class BootstrapServiceException extends InfrastructureException{

    private static final long     serialVersionUID = 1L;

    protected String              serviceClassName;

    protected static String       indentation      = "[FASTBIZ-BOOTSTRAP]";

    protected static final String CR               = System.getProperty("line.separator");

    public BootstrapServiceException(String serviceClassName, Throwable cause, String format, Object ... args) {
        super(cause, format, args);
        this.serviceClassName = serviceClassName;
    }

    public BootstrapServiceException(String serviceClassName, Throwable cause) {
        super(cause);
        this.serviceClassName = serviceClassName;
    }

    public BootstrapServiceException(String serviceClassName, String format, Object ... args) {
        this(serviceClassName, null, format, args);
    }

    public String getServiceClassName(){
        return serviceClassName;
    }

    public void setServiceClassName(String serviceClassName){
        this.serviceClassName = serviceClassName;
    }

    @Override
    public String getMessage(){
        StringWriter writer = new StringWriter(100);
        writer.write(CR);
        writer.write(indentation);
        writer.write("Bootstrap Service Class:" + getServiceClassName());
        writer.write(CR);
        writer.write(indentation);
        writer.write("Detail Message:" + super.getMessage());
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

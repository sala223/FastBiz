package com.fastbiz.core.service.provisioning;

import com.fastbiz.common.exception.InfrastructureException;

public class DataProvisioningException extends InfrastructureException{

    private static final long serialVersionUID = 1L;

    public DataProvisioningException(Throwable cause) {
        super(cause);
    }

    public DataProvisioningException(String format, Object ... args) {
        super(format, args);
    }

    public DataProvisioningException(Throwable cause, String format, Object ... args) {
        super(cause,format, args);
    }
}

package com.fastbiz.core.tenant;

import com.fastbiz.common.exception.InfrastructureException;

public class TenantAlreadyBoundException extends InfrastructureException{

    private static final long serialVersionUID = 1L;

    public TenantAlreadyBoundException(Throwable cause) {
        super(cause);
    }

    public TenantAlreadyBoundException(String format, Object ... args) {
        super(format, args);
    }

    public TenantAlreadyBoundException(Throwable cause, String format, Object ... args) {
        super(format, args, cause);
    }
}

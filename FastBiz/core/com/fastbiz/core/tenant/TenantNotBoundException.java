package com.fastbiz.core.tenant;

import com.fastbiz.common.exception.InfrastructureException;

public class TenantNotBoundException extends InfrastructureException{

    private static final long serialVersionUID = 1L;

    public TenantNotBoundException(Throwable cause) {
        super(cause);
    }

    public TenantNotBoundException(String format, Object ... args) {
        super(format, args);
    }

    public TenantNotBoundException(Throwable cause, String format, Object ... args) {
        super(format, args, cause);
    }
}

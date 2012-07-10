package com.fastbiz.common.security;

import com.fastbiz.common.exception.InfrastructureException;

public class SecurityException extends InfrastructureException{

    private static final long serialVersionUID = 1L;

    public SecurityException(Throwable cause) {
        super(cause);
    }

    public SecurityException(String format, Object ... args) {
        super(format, args);
    }

    public SecurityException(Throwable cause, String format, Object ... args) {
        super(cause, format, args);
    }
}

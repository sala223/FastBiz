package com.fastbiz.core.bootstrap.service.jndi;

import com.fastbiz.common.exception.InfrastructureException;

public class JNDIException extends InfrastructureException{

    private static final long serialVersionUID = 1L;

    public JNDIException(Throwable cause) {
        super(cause);
    }

    public JNDIException(String format, Object ... args) {
        super(format, args);
    }

    public JNDIException(Throwable cause, String format, Object ... args) {
        super(format, args, cause);
    }
}

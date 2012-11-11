package com.fastbiz.core.bootstrap.service.persistence;

import com.fastbiz.common.exception.InfrastructureException;

public class PersistenceException extends InfrastructureException{

    private static final long serialVersionUID = 1L;

    public PersistenceException(Throwable cause) {
        super(cause);
    }

    public PersistenceException(String format, Object ... args) {
        super(format, args);
    }

    public PersistenceException(Throwable cause, String format, Object ... args) {
        super(format, args, cause);
    }
}

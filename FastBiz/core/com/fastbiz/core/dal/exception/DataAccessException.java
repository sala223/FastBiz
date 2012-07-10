package com.fastbiz.core.dal.exception;

import com.fastbiz.common.exception.InfrastructureException;

public class DataAccessException extends InfrastructureException{

    private static final long serialVersionUID = 1L;

    public DataAccessException(Throwable cause) {
        super(cause);
    }

    public DataAccessException(String format, Object ... args) {
        super(format, args);
    }

    public DataAccessException(Throwable cause, String format, Object ... args) {
        super(format, args, cause);
    }
}

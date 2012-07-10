package com.fastbiz.core.bootstrap.service.datasource;

import com.fastbiz.common.exception.InfrastructureException;

public class DataSourceException extends InfrastructureException{

    private static final long serialVersionUID = 1L;

    public DataSourceException(Throwable cause) {
        super(cause);
    }

    public DataSourceException(Throwable cause, String format, Object ... args) {
        super(cause, format, args);
    }

    public DataSourceException(String format, Object ... args) {
        super(format, args);
    }
}

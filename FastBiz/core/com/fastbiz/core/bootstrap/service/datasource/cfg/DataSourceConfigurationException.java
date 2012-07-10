package com.fastbiz.core.bootstrap.service.datasource.cfg;

import com.fastbiz.common.exception.InfrastructureException;

public class DataSourceConfigurationException extends InfrastructureException{

    private static final long serialVersionUID = 1L;

    public DataSourceConfigurationException(Throwable cause) {
        super(cause);
    }

    public DataSourceConfigurationException(Throwable cause, String format, Object ... args) {
        super(cause, format, args);
    }

    public DataSourceConfigurationException(String format, Object ... args) {
        super(format, args);
    }
}

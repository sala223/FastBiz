package com.fastbiz.common.exception;

public class InfrastructureException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public InfrastructureException(Throwable cause) {
        super(cause);
    }

    public InfrastructureException(String format, Object ... args) {
        super(String.format(format, args));
    }

    public InfrastructureException(Throwable cause, String format, Object ... args) {
        super(String.format(format, args), cause);
    }
}

package com.fastbiz.common.utils.json;

import com.fastbiz.common.exception.InfrastructureException;

public class JsonException extends InfrastructureException{

    private static final long serialVersionUID = 1L;

    public JsonException(Throwable cause) {
        super(cause);
    }

    public JsonException(String format, Object ... args) {
        super(format, args);
    }

    public JsonException(Throwable cause, String format, Object ... args) {
        super(cause, format, args);
    }
}

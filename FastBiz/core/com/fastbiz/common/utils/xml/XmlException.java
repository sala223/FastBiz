package com.fastbiz.common.utils.xml;

import com.fastbiz.common.exception.InfrastructureException;

public class XmlException extends InfrastructureException{

    private static final long serialVersionUID = 1L;

    public XmlException(Throwable cause) {
        super(cause);
    }

    public XmlException(String format, Object ... args) {
        super(format, args);
    }

    public XmlException(Throwable cause, String format, Object ... args) {
        super(cause, format, args);
    }
}

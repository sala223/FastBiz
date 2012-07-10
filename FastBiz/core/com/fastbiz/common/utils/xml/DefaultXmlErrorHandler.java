package com.fastbiz.common.utils.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class DefaultXmlErrorHandler implements ErrorHandler{

    private static final Logger LOG = LoggerFactory.getLogger(DefaultXmlErrorHandler.class);

    private Object              xmlSource;

    public DefaultXmlErrorHandler(Object xmlSource) {
        this.xmlSource = xmlSource;
    }

    @Override
    public void warning(SAXParseException ex) throws SAXException{
        LOG.warn("", ex);
    }

    @Override
    public void error(SAXParseException ex) throws SAXException{
        LOG.error("Error when parsing xml source:{}", xmlSource);
        throw new XmlException(ex);
    }

    @Override
    public void fatalError(SAXParseException ex) throws SAXException{
        LOG.error("Fatal error when parsing xml source:{}", xmlSource);
        throw new XmlException(ex);
    }
}

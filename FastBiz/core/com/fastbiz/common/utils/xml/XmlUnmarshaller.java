package com.fastbiz.common.utils.xml;

import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlUnmarshaller<T> {

    private static final String SCHEMA_LANGUAGE_ATTRIBUTE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

    private static final String XSD_SCHEMA_LANGUAGE       = "http://www.w3.org/2001/XMLSchema";

    private static final Logger LOG                       = LoggerFactory.getLogger(XmlUnmarshaller.class);

    private Unmarshaller        delegate;

    private EntityResolver      entityResolver            = new ClassPathEntityResolver();

    private ErrorHandler        errorHandler;

    private boolean             validating                = true;

    private boolean             namespaceAware            = true;

    public XmlUnmarshaller(Class<?> ... classesToBeBound) {
        Assert.notEmpty(classesToBeBound);
        try {
            JAXBContext context = JAXBContext.newInstance(classesToBeBound);
            delegate = context.createUnmarshaller();
        } catch (JAXBException ex) {
            LOG.error(ex.toString());
            throw new XmlException(ex, "Cannot create JAXB Context");
        }
    }

    public XmlUnmarshaller(String ... classesToBeBound) {
        Assert.notEmpty(classesToBeBound);
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            StringBuffer buffer = new StringBuffer();
            for (String className : classesToBeBound) {
                buffer.append(className + ":");
            }
            JAXBContext context = JAXBContext.newInstance(buffer.toString(), classLoader);
            delegate = context.createUnmarshaller();
        } catch (JAXBException ex) {
            LOG.error(ex.toString());
            throw new XmlException(ex, "Cannot create JAXB Context");
        }
    }

    public XmlUnmarshaller<T> setEntityResolver(EntityResolver entityResolver){
        this.entityResolver = entityResolver;
        return this;
    }

    public XmlUnmarshaller<T> setErrorHandler(ErrorHandler errorHandler){
        this.errorHandler = errorHandler;
        return this;
    }

    public XmlUnmarshaller<T> setValidating(boolean validating){
        this.validating = validating;
        return this;
    }

    public XmlUnmarshaller<T> setNamespaceAware(boolean namespaceAware){
        this.namespaceAware = namespaceAware;
        return this;
    }

    public T unmarshall(Resource resource){
        Assert.notNull(resource);
        try {
            return unmarshall(new InputSource(resource.getInputStream()));
        } catch (IOException ex) {
            throw new XmlException(ex, "Unable reading resource file from %s", resource);
        }
    }

    public T unmarshall(InputSource inputSource){
        Assert.notNull(inputSource);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(namespaceAware);
            if (validating) {
                factory.setValidating(true);
                try {
                    factory.setAttribute(SCHEMA_LANGUAGE_ATTRIBUTE, XSD_SCHEMA_LANGUAGE);
                } catch (IllegalArgumentException ex) {
                    String fmt = new String("Unable to validate using XSD: provider [ %s" + " ] does not support XML Schema.");
                    XmlException exception = new XmlException(fmt, factory.getClass());
                    exception.initCause(ex);
                    throw exception;
                }
            } else {
                factory.setValidating(false);
            }
            DocumentBuilder builder = factory.newDocumentBuilder();
            if (errorHandler == null) {
                builder.setErrorHandler(new DefaultXmlErrorHandler(inputSource));
            } else {
                builder.setErrorHandler(errorHandler);
            }
            builder.setEntityResolver(entityResolver);
            Document document = builder.parse(inputSource);
            @SuppressWarnings("unchecked")
            T obj = (T) delegate.unmarshal(document);
            return obj;
        } catch (ParserConfigurationException ex) {
            throw new XmlException(ex);
        } catch (SAXException ex) {
            throw new XmlException(ex);
        } catch (JAXBException ex) {
            throw new XmlException(ex);
        } catch (IOException ex) {
            throw new XmlException(ex);
        }
    }

    public T unmarshall(Document document){
        Assert.notNull(document);
        try {
            @SuppressWarnings("unchecked")
            T obj = (T) delegate.unmarshal(document);
            return obj;
        } catch (JAXBException ex) {
            throw new XmlException(ex);
        }
    }

    public void setProperty(String name, Object value){
        try {
            delegate.setProperty(name, value);
        } catch (PropertyException ex) {
            throw new XmlException(ex);
        }
    }
}

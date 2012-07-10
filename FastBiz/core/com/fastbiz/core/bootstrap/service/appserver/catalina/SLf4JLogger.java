package com.fastbiz.core.bootstrap.service.appserver.catalina;

import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SLf4JLogger implements Log{

    private Logger logger;

    public SLf4JLogger(Logger logger) {
        this.logger = logger;
    }

    public SLf4JLogger(String name) {
        this.logger = LoggerFactory.getLogger(name);
    }

    public SLf4JLogger(Class<?> type) {
        this.logger = LoggerFactory.getLogger(type);
    }

    @Override
    public boolean isDebugEnabled(){
        return logger.isDebugEnabled();
    }

    @Override
    public boolean isErrorEnabled(){
        return logger.isErrorEnabled();
    }

    @Override
    public boolean isFatalEnabled(){
        return logger.isErrorEnabled();
    }

    @Override
    public boolean isInfoEnabled(){
        return logger.isInfoEnabled();
    }

    @Override
    public boolean isTraceEnabled(){
        return logger.isTraceEnabled();
    }

    @Override
    public boolean isWarnEnabled(){
        return logger.isWarnEnabled();
    }

    @Override
    public void trace(Object message){
        if (message != null) {
            logger.trace(message.toString());
        }
    }

    @Override
    public void trace(Object message, Throwable t){
        logger.trace(message == null ? "" : message.toString(), t);
    }

    @Override
    public void debug(Object message){
        if (message != null) {
            logger.debug(message.toString());
        }
    }

    @Override
    public void debug(Object message, Throwable t){
        logger.debug(message == null ? "" : message.toString(), t);
    }

    @Override
    public void info(Object message){
        if (message != null) {
            logger.info(message.toString());
        }
    }

    @Override
    public void info(Object message, Throwable t){
        logger.info(message == null ? "" : message.toString(), t);
    }

    @Override
    public void warn(Object message){
        if (message != null) {
            logger.warn(message.toString());
        }
    }

    @Override
    public void warn(Object message, Throwable t){
        logger.warn(message == null ? "" : message.toString(), t);
    }

    @Override
    public void error(Object message){
        if (message != null) {
            logger.error(message.toString());
        }
    }

    @Override
    public void error(Object message, Throwable t){
        logger.error(message == null ? "" : message.toString(), t);
    }

    @Override
    public void fatal(Object message){
        if (message != null) {
            logger.error(message.toString());
        }
    }

    @Override
    public void fatal(Object message, Throwable t){
        logger.error(message == null ? "" : message.toString(), t);
    }
}

package com.fastbiz.core.biz.exception;

import java.io.StringWriter;
import com.fastbiz.common.exception.InfrastructureException;

public class BusinessException extends InfrastructureException{

    private static final long     serialVersionUID        = 1L;

    protected int                 errorCode;

    private String              solutionId;

    protected static String       indentation             = "[FASTBIZ-SOLUTION]";

    protected static final String CR                      = System.getProperty("line.separator");

    public BusinessException(String solutionId, int errorCode, Throwable cause) {
        super(cause);
        this.solutionId = solutionId;
        this.errorCode = errorCode;
    }

    public BusinessException(String solutionId, int errorCode, String message, Object ... args) {
        super(message, args);
        this.solutionId = solutionId;
        this.errorCode = errorCode;
    }

    public String getInternalMessage(){
        return super.getMessage();
    }

    @Override
    public String getMessage(){
        StringWriter writer = new StringWriter(100);
        writer.write(CR);
        writer.write(indentation);
        writer.write("Solution ID:" + getSolutionId());
        writer.write(CR);
        writer.write(indentation);
        writer.write("Error Code:" + getErrorCode());
        writer.write(CR);
        writer.write(indentation);
        writer.write("Detail Internal Message:" + getInternalMessage());
        Throwable cause = this.getCause();
        if (cause != null && (cause instanceof java.lang.reflect.InvocationTargetException)) {
            writer.write(CR);
            writer.write(indentation);
            writer.write("Target Invocation Exception:");
            Throwable target = ((java.lang.reflect.InvocationTargetException) cause).getTargetException();
            if (target != null) {
                writer.write(target.getMessage());
            }
        }
        return writer.toString();
    }

    public int getErrorCode(){
        return errorCode;
    }

    public void setErrorCode(int errorCode){
        this.errorCode = errorCode;
    }

    public String getSolutionId(){
        return solutionId;
    }

    public void setSolutionId(String solutionId){
        this.solutionId = solutionId;
    }
}

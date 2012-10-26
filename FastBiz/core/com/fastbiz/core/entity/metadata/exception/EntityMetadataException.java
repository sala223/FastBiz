package com.fastbiz.core.entity.metadata.exception;

import java.io.StringWriter;
import com.fastbiz.common.exception.InfrastructureException;

public class EntityMetadataException extends InfrastructureException{

    private static final long  serialVersionUID = 1L;

    private String             entityName;

    public static final String CR               = System.getProperty("line.separator");

    public EntityMetadataException(Throwable cause) {
        super(cause);
    }

    public EntityMetadataException(String format, Object ... args) {
        super(format, args);
    }

    public EntityMetadataException(Throwable cause, String format, Object ... args) {
        super(format, args, cause);
    }

    public void setEntityName(String entityName){
        this.entityName = entityName;
    }

    public String getEntityName(){
        return entityName;
    }

    @Override
    public String getMessage(){
        StringWriter writer = new StringWriter(100);
        writer.write("Entity (" + entityName + ") metadata error:");
        writer.write(CR);
        writer.write(super.getMessage());
        return writer.toString();
    }
}

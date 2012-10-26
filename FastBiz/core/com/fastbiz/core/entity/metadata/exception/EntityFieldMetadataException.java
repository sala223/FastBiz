package com.fastbiz.core.entity.metadata.exception;

import java.io.StringWriter;

public class EntityFieldMetadataException extends EntityMetadataException{

    private static final long  serialVersionUID = 1L;

    private String             field;

    public static final String CR               = System.getProperty("line.separator");

    public EntityFieldMetadataException(Throwable cause) {
        super(cause);
    }

    public EntityFieldMetadataException(String format, Object ... args) {
        super(format, args);
    }

    public EntityFieldMetadataException(Throwable cause, String format, Object ... args) {
        super(format, args, cause);
    }

    public String getField(){
        return field;
    }

    public void setField(String field){
        this.field = field;
    }

    @Override
    public String getMessage(){
        StringWriter writer = new StringWriter(100);
        writer.write("Entity field (" + this.getEntityName() + ":" + this.getField() + ") metadata error:");
        writer.write(CR);
        writer.write(super.getMessage());
        return writer.toString();
    }
}

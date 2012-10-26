package com.fastbiz.core.entity.metadata;

import java.lang.reflect.Field;
import org.springframework.util.ReflectionUtils;

public class FieldEntityAttrDescriptor implements EntityAttrDescriptor{

    private Field              field;

    public static final String GET_METHOD_PREFIX = "get";

    public FieldEntityAttrDescriptor(Field field) {
        this.field = field;
    }

    @Override
    public String getName(){
        return field.getName();
    }

    @Override
    public Object accessValue(Object entity){
        return ReflectionUtils.getField(field, entity);
    }
}

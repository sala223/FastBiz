package com.fastbiz.core.entity.metadata;

import java.lang.reflect.Method;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

public class ExtendedEntityAttrDescriptor implements EntityAttrDescriptor{

    private String              name;

    private String              accessMethodName;

    private EntityAttrType      type;

    private static final String STRING_ATTR_ACCESS_METHOD_NAME  = "getStringAttribute";

    private static final String DECIMAL_ATTR_ACCESS_METHOD_NAME = "getDecimalAttribute";

    private static final String DATE_ATTR_ACCESS_METHOD_NAME    = "getDateTimeAttributes";

    private static final String BLOB_ATTR_ACCESS_METHOD_NAME    = "getBlobAttribute";

    public ExtendedEntityAttrDescriptor(String name, EntityAttrType type) {
        this.name = name;
        this.type = type;
    }

    public String getAccessMethodName(){
        if (accessMethodName == null) {
            switch (type) {
            case DATE:
                return DATE_ATTR_ACCESS_METHOD_NAME;
            case DECIMAL:
                return DECIMAL_ATTR_ACCESS_METHOD_NAME;
            case MONEY:
                return DECIMAL_ATTR_ACCESS_METHOD_NAME;
            case IMAGE:
                return BLOB_ATTR_ACCESS_METHOD_NAME;
            default:
                return STRING_ATTR_ACCESS_METHOD_NAME;
            }
        } else {
            return accessMethodName;
        }
    }

    public void setAccessMethodName(String accessMethodName){
        this.accessMethodName = accessMethodName;
    }

    @Override
    public String getName(){
        return this.name;
    }

    @Override
    public Object accessValue(Object entity){
        Assert.notNull(entity);
        Method method = ReflectionUtils.findMethod(entity.getClass(), this.getAccessMethodName(), String.class);
        return ReflectionUtils.invokeMethod(method, entity);
    }
}

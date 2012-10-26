package com.fastbiz.core.entity.metadata;

import java.lang.reflect.Method;
import org.springframework.util.ReflectionUtils;
import com.fastbiz.core.entity.metadata.exception.EntityFieldMetadataException;

public class PropertyEntityAttrDescriptor implements EntityAttrDescriptor{

    private Method             getterMethod;

    public static final String GET_METHOD_PREFIX = "get";

    public PropertyEntityAttrDescriptor(Method getterMethod) {
        checkMethod(getterMethod);
        this.getterMethod = getterMethod;
    }

    protected void checkMethod(Method method){
        if (getterMethod.getName().length() <= GET_METHOD_PREFIX.length()) {
            String fmt = "method name %s length must be greater than " + GET_METHOD_PREFIX.length();
            EntityFieldMetadataException exception = new EntityFieldMetadataException(fmt, getterMethod.getName());
            throw exception;
        }
        if (!getterMethod.getName().startsWith(GET_METHOD_PREFIX)) {
            String fmt = "method name %s must be begin with " + GET_METHOD_PREFIX;
            EntityFieldMetadataException exception = new EntityFieldMetadataException(fmt, getterMethod.getName());
            throw exception;
        }
        if (getterMethod.getParameterTypes().length != 0) {
            String fmt = "method should not take any parameter";
            EntityFieldMetadataException exception = new EntityFieldMetadataException(fmt);
            throw exception;
        }
    }

    @Override
    public String getName(){
        return getterMethod.getName().substring(3);
    }

    @Override
    public Object accessValue(Object entity){
        return ReflectionUtils.invokeMethod(getterMethod, entity);
    }
}

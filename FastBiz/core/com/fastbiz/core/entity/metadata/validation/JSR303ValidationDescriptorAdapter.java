package com.fastbiz.core.entity.metadata.validation;

import java.lang.annotation.Annotation;
import java.util.Map;
import javax.validation.metadata.ConstraintDescriptor;
import org.apache.bval.jsr303.ConstraintAnnotationAttributes;

public class JSR303ValidationDescriptorAdapter<T extends Annotation> implements EntityAttrValidationDescriptor{

    private ConstraintDescriptor<T> descriptor;

    public JSR303ValidationDescriptorAdapter(ConstraintDescriptor<T> descriptor) {
        this.descriptor = descriptor;
    }

    public ConstraintDescriptor<T> getConstraintDescriptor(){
        return descriptor;
    }

    public T getAnnotation(){
        return this.descriptor.getAnnotation();
    }

    @Override
    public Map<String, Object> getAttributes(){
        return descriptor.getAttributes();
    }

    @Override
    public String getErrorMessage(){
        return ConstraintAnnotationAttributes.MESSAGE.get(getAttributes());
    }

    @Override
    public ValidationMetaBean getValidationMetadata(){
        return null;
    }
}

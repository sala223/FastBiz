package com.fastbiz.core.entity.validation;

import com.fastbiz.core.entity.metadata.validation.EntityAttrValidationDescriptor;

public interface ValidationDescriptorRepository{

    EntityAttrValidationDescriptor getAttrValidationDescriptor(String object,String attributeName);
    
}

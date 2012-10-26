package com.fastbiz.core.entity.metadata.validation;

import java.util.Map;

public interface EntityAttrValidationDescriptor{

    String getErrorMessage(); 

    Map<String, Object> getAttributes();

    ValidationMetaBean getValidationMetadata();
}

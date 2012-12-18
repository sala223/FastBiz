package com.fastbiz.core.entity.validation;

import com.fastbiz.core.entity.metadata.EntityExtendedAttrDescriptor;

public interface EntityExtendedAttrFactory{

    EntityExtendedAttrDescriptor[] getEntityExtendedAttrs(Class<?> entityType);
}

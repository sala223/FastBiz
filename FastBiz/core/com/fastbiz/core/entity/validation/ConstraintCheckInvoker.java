package com.fastbiz.core.entity.validation;

import com.fastbiz.core.entity.metadata.EntityExtendedAttrConstraint;

public interface ConstraintCheckInvoker{

    boolean invokeCheck(CheckInvokerContext context, EntityExtendedAttrConstraint constraint);
}

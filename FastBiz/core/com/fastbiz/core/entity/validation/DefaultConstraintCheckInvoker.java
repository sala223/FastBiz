package com.fastbiz.core.entity.validation;

import com.fastbiz.core.entity.metadata.EntityExtendedAttrConstraint;

public class DefaultConstraintCheckInvoker implements ConstraintCheckInvoker{

    @Override
    public boolean invokeCheck(CheckInvokerContext context, EntityExtendedAttrConstraint constraint){
        return true;
    }
}

package com.fastbiz.core.entity.validation;

import com.fastbiz.core.entity.metadata.EntityExtendedAttrConstraint;

public class ConstraintCheckInvokerFactory{

    public ConstraintCheckInvoker createConstraintCheckInvoker(EntityExtendedAttrConstraint constraint){
        return new DefaultConstraintCheckInvoker();
    }
}

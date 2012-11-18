package com.fastbiz.core.entity.metadata;

import net.sf.oval.Check;
import net.sf.oval.constraint.MinLengthCheck;
import net.sf.oval.constraint.NotNullCheck;

public enum ConstraintCheckType {
    NotNull(NotNullCheck.class), MaxLength(MinLengthCheck.class);

    private Class<? extends Check> checkClass;

    private ConstraintCheckType(Class<? extends Check> checkClass) {
        this.checkClass = checkClass;
    }

    public Class<? extends Check> getCheckClass(){
        return checkClass;
    }
}

package com.fastbiz.core.entity.validation;

import java.lang.annotation.Annotation;
import javax.validation.ConstraintValidator;

public interface JSR303AnnotationValidatorLookup{

    boolean containsConstraintValidator(Class<? extends Annotation> annotationClass);

    <A extends Annotation> Class<? extends ConstraintValidator<A, ?>>[] getConstraintValidators(Class<A> annotationClass);
}

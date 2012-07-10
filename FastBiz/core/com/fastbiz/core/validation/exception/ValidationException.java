package com.fastbiz.core.validation.exception;

import java.io.StringWriter;
import javax.validation.ConstraintViolation;
import com.fastbiz.core.biz.exception.BusinessException;

@SuppressWarnings("rawtypes")
public class ValidationException extends BusinessException{

    private static final long       serialVersionUID = 1L;

    protected ConstraintViolation[] violations;

    public ValidationException(String solutionId, ConstraintViolation[] violations) {
        super(solutionId, ValidationException.ENTITY_VALIDATION_ERROR, null);
        this.violations = violations;
    }

    public ConstraintViolation[] getViolations(){
        return violations;
    }

    @Override
    public String getInternalMessage(){
        StringWriter writer = new StringWriter(100);
        writer.write(CR);
        if (violations != null && violations.length > 0) {
            writer.write("Root Bean Class:" + violations[0].getRootBeanClass());
        }
        if (violations != null) {
            for (ConstraintViolation<?> violation : violations) {
                writer.write(CR);
                writer.write(violation.getPropertyPath() + ":" + violation.getMessage());
            }
        }
        return writer.toString();
    }
}

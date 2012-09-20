package com.fastbiz.core.validation.exception;

import java.io.StringWriter;
import javax.validation.ConstraintViolation;
import com.fastbiz.common.exception.InfrastructureException;

public class ValidationException extends InfrastructureException{

    private static final long     serialVersionUID = 1L;

    private ConstraintViolation<?>[] violations;

    private String                solutionId;

    protected static final String CR               = System.getProperty("line.separator");

    public ValidationException(String solutionId, ConstraintViolation<?>[] violations) {
        super(null);
        this.violations = violations;
        this.solutionId = solutionId;
    }

    public ConstraintViolation<?>[] getViolations(){
        return violations;
    }

    public String getSolutionId(){
        return solutionId;
    }

    public void setSolutionId(String solutionId){
        this.solutionId = solutionId;
    }

    @Override
    public String getMessage(){
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

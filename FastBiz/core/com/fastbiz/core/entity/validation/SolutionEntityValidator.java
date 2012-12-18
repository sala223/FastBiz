package com.fastbiz.core.entity.validation;

import java.util.List;
import com.fastbiz.core.entity.ExtensibleEntity;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrConstraint;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrDescriptor;
import net.sf.oval.ConstraintViolation;
import net.sf.oval.Validator;
import net.sf.oval.configuration.Configurer;
import net.sf.oval.exception.ValidationFailedException;

public class SolutionEntityValidator extends Validator{

    private EntityExtendedAttrFactory     entityExtendedAttrFactory;

    private ConstraintCheckInvokerFactory constraintCheckInvokerFactory;

    public SolutionEntityValidator() {}

    public SolutionEntityValidator(Configurer ... configurers) {
        super(configurers);
    }

    public void setEntityExtendedAttrFactory(EntityExtendedAttrFactory entityExtendedAttrFactory){
        this.entityExtendedAttrFactory = entityExtendedAttrFactory;
    }

    public void setConstraintCheckInvokerFactory(ConstraintCheckInvokerFactory constraintCheckInvokerFactory){
        this.constraintCheckInvokerFactory = constraintCheckInvokerFactory;
    }

    @Override
    public List<ConstraintViolation> validate(Object validatedObject) throws IllegalArgumentException,
                    ValidationFailedException{
        List<ConstraintViolation> violations = super.validate(validatedObject);
        if (validatedObject instanceof ExtensibleEntity) {
            validateExtensibleAttributes((ExtensibleEntity) validatedObject, violations);
        }
        return violations;
    }

    protected void validateExtensibleAttributes(ExtensibleEntity entity, List<ConstraintViolation> violations){
        EntityExtendedAttrDescriptor[] attrs = entityExtendedAttrFactory.getEntityExtendedAttrs(entity.getClass());
        if (attrs != null) {
            for (EntityExtendedAttrDescriptor attr : attrs) {
                Object attrValue = attr.accessValue(entity);
                List<EntityExtendedAttrConstraint> constraints = attr.getConstraints();
                if (constraints != null) {
                    CheckInvokerContext context = new CheckInvokerContext(entity, attr, attrValue);
                    for (EntityExtendedAttrConstraint constraint : constraints) {
                        ConstraintCheckInvoker invoker = constraintCheckInvokerFactory.createConstraintCheckInvoker(constraint);
                        invoker.invokeCheck(context, constraint);
                    }
                }
            }
        }
    }
}

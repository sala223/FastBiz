package com.fastbiz.core.entity.validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.ConstraintViolation;
import org.apache.bval.jsr303.ApacheFactoryContext;
import org.apache.bval.jsr303.ClassValidator;
import com.fastbiz.core.entity.ExtensibleEntity;

public class EntityValidator extends ClassValidator{

    private static Map<String, List<Field>> extensionAttributesFields = new ConcurrentHashMap<String, List<Field>>();

    public EntityValidator(ApacheFactoryContext factoryContext) {
        super(factoryContext);
    }

    @Override
    public <T> Set<ConstraintViolation<T>> validate(T object, Class<?> ... groups){
        Set<ConstraintViolation<T>> violations = super.validate(object, groups);
        if (ExtensibleEntity.class.isAssignableFrom(object.getClass())) {
            if (groups.length == 0) {
                violations.addAll(validateExtensionAttributes(object));
            }
        }
        return violations;
    }

    public <T> Set<ConstraintViolation<T>> validateExtensionAttributes(T object){
        List<Field> fields = decideEtensionAttributeFields(object);
        
        return null;
    }

    protected List<Field> decideEtensionAttributeFields(Object object){
        Class<?> entityClass = object.getClass();
        if (!extensionAttributesFields.containsKey(entityClass.getName())) {
            synchronized (object.getClass()) {
                if (!extensionAttributesFields.containsKey(entityClass.getName())) {
                    Field[] fields = entityClass.getFields();
                    ArrayList<Field> extensionFields = new ArrayList<Field>();
                    for (Field field : fields) {
                        if (field.getAnnotation(ExtensionConstraint.class) != null) {
                            if (field.getType() == Map.class) {
                                extensionFields.add(field);
                            }
                        }
                    }
                    extensionAttributesFields.put(entityClass.getName(), extensionFields);
                }
            }
        }
        return extensionAttributesFields.get(entityClass.getName());
    }
}

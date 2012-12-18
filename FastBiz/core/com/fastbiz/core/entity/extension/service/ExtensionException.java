package com.fastbiz.core.entity.extension.service;

import com.fastbiz.core.biz.exception.BusinessException;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrDescriptor;

public class ExtensionException extends BusinessException{

    private static final long   serialVersionUID                                                  = 1L;

    private static final String ERROR_CODE_NO_SUCH_ENTITY_TYPE                                    = "NO_SUCH_ENTITY_TYPE";

    private static final String ERROR_CODE_NOT_AN_ENTITY_CLASS                                    = "NOT_AN_ENTITY_CLASS";

    private static final String ERROR_CODE_ENTITY_ATTR_ALREADY_EXIST                              = "ENTITY_ATTR_ALREADY_EXIST";

    private static final String ERROR_CODE_ENTITY_ATTR_NOT_EXIST                                  = "ENTITY_ATTR_NOT_EXIST";

    private static final String ERROR_CODE_ENTITY_ATTR_OWNER_UNMODIFIABLE                         = "ENTITY_ATTR_OWNER_UNMODIFIABLE";

    private static final String ERROR_CODE_ENTITY_ATTR_CONSTRAINT_PARAMETER_EXCEED_MAX_LIMITATION = "ERROR_CODE_ENTITY_ATTR_CONSTRAINT_PARAMETER_EXCEED_MAX_LIMITATION";

    public ExtensionException(String errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public ExtensionException(String errorCode, String message, Object ... args) {
        super(errorCode, message, args);
    }

    public static ExtensionException noSuchEntityTypeException(String entity){
        return new ExtensionException(ERROR_CODE_NO_SUCH_ENTITY_TYPE, "Entity type %s does not exist", entity);
    }

    public static ExtensionException EntityAttributeNotExist(long attributeId){
        return new ExtensionException(ERROR_CODE_ENTITY_ATTR_NOT_EXIST, "Entity attribute ID=%s does not exist", attributeId);
    }

    public static ExtensionException EntityAttributeOwnerUnModifiable(EntityExtendedAttrDescriptor attr, String orginalEntity){
        String fmt = "The owner of entity attribute ID=%s cannot be changed from %s to %s";
        return new ExtensionException(ERROR_CODE_ENTITY_ATTR_OWNER_UNMODIFIABLE, fmt, orginalEntity, attr.getEntityName());
    }

    public static ExtensionException EntityAttributeNotExist(String attributeName){
        String fmt = "Entity attribute NAME=%s does not exist";
        return new ExtensionException(ERROR_CODE_ENTITY_ATTR_NOT_EXIST, fmt, attributeName);
    }

    public static ExtensionException notAnEntityClassException(Class<?> entityClass){
        return new ExtensionException(ERROR_CODE_NOT_AN_ENTITY_CLASS, "Class %s is not an entity", entityClass.getName());
    }

    public static ExtensionException EntityAttributeAlreadyExistException(String entityName, String attr){
        String fmt = "Entity %s Attribute %s already exist";
        return new ExtensionException(ERROR_CODE_ENTITY_ATTR_ALREADY_EXIST, fmt, entityName, attr);
    }

    public static ExtensionException EntityAttributeConstraintParameterMaxLimitationException(){
        String fmt = "Entity attribute constraint parameter exceed max size limitation";
        return new ExtensionException(ERROR_CODE_ENTITY_ATTR_CONSTRAINT_PARAMETER_EXCEED_MAX_LIMITATION, fmt);
    }
}

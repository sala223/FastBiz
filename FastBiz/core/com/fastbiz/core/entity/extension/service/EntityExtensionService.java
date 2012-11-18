package com.fastbiz.core.entity.extension.service;

import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fastbiz.core.entity.extension.dal.ExtensibleEntityMetadataDAL;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrDescriptor;

@Service("entityExtensionService")
public class EntityExtensionService{

    @Autowired
    protected ExtensibleEntityMetadataDAL extensibleEntityAttributeDAL;

    @Transactional
    public List<EntityExtendedAttrDescriptor> getExtendedAttributes(Class<?> entityClass){
        return extensibleEntityAttributeDAL.getExtendedAttributes(entityClass);
    }

    @Transactional
    public List<EntityExtendedAttrDescriptor> getExtendedAttributes(String entityName){
        return extensibleEntityAttributeDAL.getExtendedAttributes(entityName);
    }

    @Transactional
    public EntityExtendedAttrDescriptor getExtendedAttribute(Class<?> entityClass, String attribute){
        return extensibleEntityAttributeDAL.getExtendedAttribute(entityClass, attribute);
    }

    @Transactional
    public EntityExtendedAttrDescriptor getExtendedAttribute(String entityName, String attribute){
        return extensibleEntityAttributeDAL.getExtendedAttribute(entityName, attribute);
    }

    @Transactional
    public String getEntityAlias(Class<?> entityClass){
        return extensibleEntityAttributeDAL.getEntityAlias(entityClass);
    }

    @Transactional
    public void addEntityAttributes(String entityName, List<EntityExtendedAttrDescriptor> attributes){
        if (attributes == null || attributes.size() == 0) {
            return;
        }
        for (EntityExtendedAttrDescriptor attr : attributes) {
            attr.setEntityName(entityName);
        }
        extensibleEntityAttributeDAL.createEntityAttributes(entityName, attributes);
    }

    @Transactional
    public void addEntityAttributes(Class<?> entityClass, List<EntityExtendedAttrDescriptor> attributes){
        if (attributes == null || attributes.size() == 0) {
            return;
        }
        for (EntityExtendedAttrDescriptor attr : attributes) {
            attr.setEntityName(this.getEntityAlias(entityClass));
        }
        extensibleEntityAttributeDAL.createEntityAttributes(entityClass, attributes);
    }

    @Transactional
    public void addEntityAttributes(String entityName, EntityExtendedAttrDescriptor ... attrs){
        if (attrs == null || attrs.length == 0) {
            return;
        }
        extensibleEntityAttributeDAL.createEntityAttributes(entityName, Arrays.asList(attrs));
    }

    @Transactional
    public int deleteEntityAttributes(Class<?> entityClass, String ... attributes){
        return extensibleEntityAttributeDAL.deleteEntityAttibutes(entityClass, attributes);
    }
}

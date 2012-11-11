package com.fastbiz.core.service.extension;

import java.util.List;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fastbiz.core.dal.ExtensibleEntityMetadataDAL;
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
        ClassDescriptor descriptor = extensibleEntityAttributeDAL.getEntityDescriptor(entityName);
        if (descriptor == null) {
            throw ExtensionException.noSuchEntityException(entityName);
        }
        return extensibleEntityAttributeDAL.getExtendedAttributes(entityName);
    }

    @Transactional
    public String getEntityAlias(Class<?> entityClass){
        return extensibleEntityAttributeDAL.getEntityAlias(entityClass);
    }

    @Transactional
    public void addEntityAttributes(List<EntityExtendedAttrDescriptor> attributes){
        this.addEntityAttributes(attributes);
    }

    @Transactional
    public int deleteEntityAttributes(Class<?> entityClass, String[] attributes){
        return extensibleEntityAttributeDAL.deleteEntityAttibutes(entityClass, attributes);
    }
}

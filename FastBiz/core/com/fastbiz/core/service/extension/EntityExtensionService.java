package com.fastbiz.core.service.extension;

import java.util.List;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fastbiz.core.dal.ExtensibleEntityAttributeDAL;
import com.fastbiz.core.entity.ExtensibleEntityAttribute;

@Service("entityExtensionService")
public class EntityExtensionService{

    @Autowired
    protected ExtensibleEntityAttributeDAL extensibleEntityAttributeDAL;

    @Transactional
    public List<ExtensibleEntityAttribute> getExtendedAttributes(Class<?> entityClass){
        return extensibleEntityAttributeDAL.getExtendedAttributes(entityClass);
    }

    @Transactional
    public List<ExtensibleEntityAttribute> getExtendedAttributes(String entityName){
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
    public void addEntityAttributes(List<ExtensibleEntityAttribute> attributes){
        this.addEntityAttributes(attributes);
    }

    @Transactional
    public int deleteEntityAttributes(Class<?> entityClass, String[] attributes){
        return extensibleEntityAttributeDAL.deleteEntityAttibutes(entityClass, attributes);
    }
}

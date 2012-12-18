package com.fastbiz.core.entity.extension.service;

import java.util.Arrays;
import java.util.List;
import javax.jws.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fastbiz.core.entity.extension.dal.ExtensibleEntityMetadataDAL;
import com.fastbiz.core.entity.extension.service.contract.IEntityExtensionService;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrDescriptor;

@Service("entityExtensionService")
@Transactional
@WebService(endpointInterface = "com.fastbiz.core.entity.extension.service.contract.IEntityExtensionService", serviceName = "entityExtensionService")
public class EntityExtensionService implements IEntityExtensionService{

    @Autowired
    protected ExtensibleEntityMetadataDAL extensibleEntityAttributeDAL;

    public List<EntityExtendedAttrDescriptor> getExtendedAttributes(String entityName){
        return extensibleEntityAttributeDAL.getExtendedAttributes(entityName);
    }

    public EntityExtendedAttrDescriptor getExtendedAttribute(String entityName, String attribute){
        return extensibleEntityAttributeDAL.getExtendedAttribute(entityName, attribute);
    }

    public String getEntityAlias(Class<?> entityClass){
        return extensibleEntityAttributeDAL.getEntityAlias(entityClass);
    }

    public void addEntityAttributes(String entityName, List<EntityExtendedAttrDescriptor> attributes){
        if (attributes == null || attributes.size() == 0) {
            return;
        }
        for (EntityExtendedAttrDescriptor attr : attributes) {
            attr.setEntityName(entityName);
        }
        extensibleEntityAttributeDAL.createEntityAttributes(entityName, attributes);
    }

    public void addEntityAttributes(String entityName, EntityExtendedAttrDescriptor ... attrs){
        if (attrs == null || attrs.length == 0) {
            return;
        }
        for (EntityExtendedAttrDescriptor attr : attrs) {
            attr.setEntityName(entityName);
        }
        extensibleEntityAttributeDAL.createEntityAttributes(entityName, Arrays.asList(attrs));
    }

    public int deleteEntityAttributes(String entityName, String ... attributes){
        return extensibleEntityAttributeDAL.deleteEntityAttibutes(entityName, attributes);
    }

    @Override
    public void updateEntityAttribute(EntityExtendedAttrDescriptor attr){
        extensibleEntityAttributeDAL.updateEntityAttribute(attr);
    }

    @Override
    public void updateEntityAttributes(EntityExtendedAttrDescriptor ... attrs){
        for (EntityExtendedAttrDescriptor attr : attrs) {
            extensibleEntityAttributeDAL.updateEntityAttribute(attr);
        }
    }
}

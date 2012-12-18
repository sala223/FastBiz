package com.fastbiz.core.entity.extension.dal;

import java.util.Arrays;
import java.util.List;
import javax.persistence.Query;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.springframework.stereotype.Repository;
import com.fastbiz.core.dal.EclipseLinkDataAccessFoundation;
import com.fastbiz.core.entity.extension.service.ExtensionException;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrDescriptor;

@Repository
@SuppressWarnings("unchecked")
public class ExtensibleEntityMetadataDAL extends EclipseLinkDataAccessFoundation{

    public void createEntityAttributes(String entityName, List<EntityExtendedAttrDescriptor> newAttrs){
        checkEntityValidity(entityName);
        List<EntityExtendedAttrDescriptor> extendedAttributes = this.getExtendedAttributes(entityName);
        for (EntityExtendedAttrDescriptor attr : extendedAttributes) {
            for (EntityExtendedAttrDescriptor newAttr : newAttrs) {
                if (attr.getName().equals(newAttr)) {
                    throw ExtensionException.EntityAttributeAlreadyExistException(entityName, newAttr.getName());
                }
            }
        }
        this.bulkInsert(newAttrs);
    }

    public List<EntityExtendedAttrDescriptor> getExtendedAttributes(String entityName){
        checkEntityValidity(entityName);
        String queryName = EntityExtendedAttrDescriptor.NQ_FIND_EXTENDED_ATTRS_BY_ENTITY_NAME;
        Query query = this.getEntityManager().createNamedQuery(queryName);
        query.setParameter("ENTITY_NAME", entityName);
        return query.getResultList();
    }

    public EntityExtendedAttrDescriptor getExtendedAttribute(String entityName, String attributeName){
        checkEntityValidity(entityName);
        String queryName = EntityExtendedAttrDescriptor.NQ_FIND_EXTENDED_ATTR_BY_ATTR_NAME;
        Query query = this.getEntityManager().createNamedQuery(queryName);
        query.setParameter("ENTITY_NAME", entityName);
        query.setParameter("ATTR_NAME", attributeName);
        return this.executeSingleQuery(query);
    }

    public int deleteEntityAttibutes(String entityName, String[] attributes){
        checkEntityValidity(entityName);
        if (attributes == null || attributes.length == 0) {
            return 0;
        }
        String fmt = "DELETE from %s u WHERE u.entityName=:ENTITY_NAME and u.name IN :ATTRIBUTE_NAMES";
        String sql = String.format(fmt, getEntityAlias(EntityExtendedAttrDescriptor.class));
        Query query = this.getEntityManager().createQuery(sql);
        query.setParameter("ENTITY_NAME", entityName);
        query.setParameter("ATTRIBUTE_NAMES", Arrays.asList(attributes));
        return query.executeUpdate();
    }

    public void updateEntityAttribute(EntityExtendedAttrDescriptor attr){
        checkEntityValidity(attr.getEntityName());
        EntityExtendedAttrDescriptor old = this.find(EntityExtendedAttrDescriptor.class, attr.getId());
        if (old == null) {
            throw ExtensionException.EntityAttributeNotExist(attr.getId());
        }
        if (old.getEntityName().equals(attr.getEntityName())) {
            throw ExtensionException.EntityAttributeOwnerUnModifiable(attr, old.getEntityName());
        }
        this.update(attr);
    }

    public String getEntityAlias(Class<?> entityClass){
        ClassDescriptor descriptor = this.getClassDescrptor(entityClass);
        if (descriptor == null) {
            throw ExtensionException.notAnEntityClassException(entityClass);
        }
        return this.getClassDescrptor(entityClass).getAlias();
    }

    protected ClassDescriptor getEntityDescriptor(String entityName){
        return this.getClassDescrptor(entityName);
    }

    protected void checkEntityValidity(String entityName){
        ClassDescriptor descriptor = this.getEntityDescriptor(entityName);
        if (descriptor == null) {
            throw ExtensionException.noSuchEntityTypeException(entityName);
        }
    }
}

package com.fastbiz.core.entity.extension.dal;

import java.util.Arrays;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

    public void createEntityAttributes(Class<?> entityClass, List<EntityExtendedAttrDescriptor> newAttrs){
        ClassDescriptor descriptor = checkEntityValidity(entityClass);
        createEntityAttributes(descriptor.getAlias(), newAttrs);
    }

    public List<EntityExtendedAttrDescriptor> getExtendedAttributes(Class<?> entityClass){
        checkEntityValidity(entityClass);
        String alias = this.getClassDescrptor(entityClass).getAlias();
        return this.getExtendedAttributes(alias);
    }

    public List<EntityExtendedAttrDescriptor> getExtendedAttributes(String entityName){
        checkEntityValidity(entityName);
        String queryName = EntityExtendedAttrDescriptor.NQ_FIND_EXTENDED_ATTRS_BY_ENTITY_NAME;
        Query query = this.getEntityManager().createNamedQuery(queryName);
        query.setParameter("ENTITY_NAME", entityName);
        return query.getResultList();
    }

    public EntityExtendedAttrDescriptor getExtendedAttribute(Class<?> entityClass, String attributeName){
        checkEntityValidity(entityClass);
        String alias = this.getClassDescrptor(entityClass).getAlias();
        return this.getExtendedAttribute(alias, attributeName);
    }

    public EntityExtendedAttrDescriptor getExtendedAttribute(String entityName, String attributeName){
        checkEntityValidity(entityName);
        CriteriaBuilder builder = createQueryBuilder();
        CriteriaQuery<EntityExtendedAttrDescriptor> query = builder.createQuery(EntityExtendedAttrDescriptor.class);
        Root<EntityExtendedAttrDescriptor> root = query.from(EntityExtendedAttrDescriptor.class);
        query.where(builder.equal(root.get("entityName"), entityName));
        query.where(builder.equal(root.get("name"), attributeName));
        return this.executeSingleQuery(query);
    }

    public int deleteEntityAttibutes(Class<?> entityClass, String[] attributes){
        ClassDescriptor descriptor = checkEntityValidity(entityClass);
        return deleteEntityAttibutes(descriptor.getAlias(), attributes);
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

    public String getEntityAlias(Class<?> entityClass){
        this.checkEntityValidity(entityClass);
        return this.getClassDescrptor(entityClass).getAlias();
    }

    protected ClassDescriptor getEntityDescriptor(String entityName){
        return this.getClassDescrptor(entityName);
    }

    protected ClassDescriptor checkEntityValidity(Class<?> entityClass){
        ClassDescriptor descriptor = this.getClassDescrptor(entityClass);
        if (descriptor == null) {
            throw ExtensionException.notAnEntityClassException(entityClass);
        }
        return descriptor;
    }

    protected void checkEntityValidity(String entityName){
        ClassDescriptor descriptor = this.getEntityDescriptor(entityName);
        if (descriptor == null) {
            throw ExtensionException.noSuchEntityException(entityName);
        }
    }
}

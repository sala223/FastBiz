package com.fastbiz.core.dal;

import java.util.List;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.springframework.stereotype.Repository;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrDescriptor;

@Repository
public class ExtensibleEntityMetadataDAL extends EclipseLinkDataAccessFoundation{

    public List<EntityExtendedAttrDescriptor> getExtendedAttributes(Class<?> entityClass){
        String alias = this.getClassDescrptor(entityClass).getAlias();
        return this.getExtendedAttributes(alias);
    }

    public List<EntityExtendedAttrDescriptor> getExtendedAttributes(String entityName){
        CriteriaBuilder builder = createQueryBuilder();
        CriteriaQuery<EntityExtendedAttrDescriptor> query = builder.createQuery(EntityExtendedAttrDescriptor.class);
        Root<EntityExtendedAttrDescriptor> root = query.from(EntityExtendedAttrDescriptor.class);
        query.where(builder.equal(root.get("entity"), entityName));
        return this.executeQuery(query);
    }

    public EntityExtendedAttrDescriptor getExtendedAttribute(String entityName, String attributeName){
        CriteriaBuilder builder = createQueryBuilder();
        CriteriaQuery<EntityExtendedAttrDescriptor> query = builder.createQuery(EntityExtendedAttrDescriptor.class);
        Root<EntityExtendedAttrDescriptor> root = query.from(EntityExtendedAttrDescriptor.class);
        query.where(builder.equal(root.get("entity"), entityName));
        query.where(builder.equal(root.get("name"), attributeName));
        return this.executeSingleQuery(query);
    }

    public int deleteEntityAttibutes(Class<?> entityClass, String[] attributes){
        if (attributes == null || attributes.length == 0) {
            return 0;
        }
        String fmt = "DELETE %s u WHERE u.name IN :ATTRIBUTE_NAMES";
        String sql = String.format(fmt, getEntityAlias(entityClass));
        Query query = this.getEntityManager().createQuery(sql);
        query.setParameter("ATTRIBUTE_NAMES", attributes);
        return query.executeUpdate();
    }

    public void addEntityAttibutes(List<EntityExtendedAttrDescriptor> attributes){
        if (attributes != null && attributes.size() > 0) {
            this.bulkInsert(attributes);
        }
    }

    public String getEntityAlias(Class<?> entityClass){
        return this.getClassDescrptor(entityClass).getAlias();
    }

    public ClassDescriptor getEntityDescriptor(String entityName){
        return this.getClassDescrptor(entityName);
    }
}

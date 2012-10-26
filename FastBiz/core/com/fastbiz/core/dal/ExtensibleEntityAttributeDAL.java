package com.fastbiz.core.dal;

import java.util.List;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.springframework.stereotype.Repository;
import com.fastbiz.core.entity.ExtensibleEntityAttribute;

@Repository
public class ExtensibleEntityAttributeDAL extends EclipseLinkDataAccessFoundation{

    public List<ExtensibleEntityAttribute> getExtendedAttributes(Class<?> entityClass){
        String alias = this.getClassDescrptor(entityClass).getAlias();
        return this.getExtendedAttributes(alias);
    }

    public List<ExtensibleEntityAttribute> getExtendedAttributes(String entityName){
        CriteriaBuilder builder = createQueryBuilder();
        CriteriaQuery<ExtensibleEntityAttribute> query = builder.createQuery(ExtensibleEntityAttribute.class);
        Root<ExtensibleEntityAttribute> root = query.from(ExtensibleEntityAttribute.class);
        query.where(builder.equal(root.get("entity"), entityName));
        return this.executeQuery(query);
    }

    public ExtensibleEntityAttribute getExtendedAttribute(String entityName, String attributeName){
        CriteriaBuilder builder = createQueryBuilder();
        CriteriaQuery<ExtensibleEntityAttribute> query = builder.createQuery(ExtensibleEntityAttribute.class);
        Root<ExtensibleEntityAttribute> root = query.from(ExtensibleEntityAttribute.class);
        query.where(builder.equal(root.get("entity"), entityName));
        query.where(builder.equal(root.get("attributeName"), attributeName));
        return this.executeSingleQuery(query);
    }

    public int deleteEntityAttibutes(Class<?> entityClass, String[] attributes){
        if (attributes == null || attributes.length == 0) {
            return 0;
        }
        String fmt = "DELETE %s u WHERE u.attributeName IN :ATTRIBUTE_NAMES";
        String sql = String.format(fmt, getEntityAlias(entityClass));
        Query query = this.getEntityManager().createQuery(sql);
        query.setParameter("ATTRIBUTE_NAMES", attributes);
        return query.executeUpdate();
    }

    public void addEntityAttibutes(List<ExtensibleEntityAttribute> attributes){
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

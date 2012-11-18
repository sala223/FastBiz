package com.fastbiz.core.entity.metadata;

import java.lang.reflect.Method;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import com.fastbiz.core.entity.MultiTenantSupport;
import org.eclipse.persistence.annotations.Index;
import org.eclipse.persistence.config.QueryHints;

;
@Entity
@Table(name = "ENTITY_EXT_ATTR")
@NamedQuery(name = EntityExtendedAttrDescriptor.NQ_FIND_EXTENDED_ATTRS_BY_ENTITY_NAME, query = "SELECT e FROM EntityExtendedAttrDescriptor e where e.entityName=:ENTITY_NAME", hints = { @QueryHint(name = QueryHints.BATCH, value = "e.constraintSet.constraints") })
public class EntityExtendedAttrDescriptor extends MultiTenantSupport implements EntityAttrDescriptor{

    public static final String              NQ_FIND_EXTENDED_ATTRS_BY_ENTITY_NAME = "findAttrsByEntityName";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int                             id;

    @Column(length = 56)
    @Index
    private String                          entityName;

    @Column(length = 56)
    private String                          name;

    @Column(length = 56)
    private String                          display;

    @Transient
    private String                          accessMethodName;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private EntityAttrType                  type;

    @Column(length = 256)
    private String                          description;

    @Embedded
    private EntityExtendedAttrConstraintSet constraintSet = new EntityExtendedAttrConstraintSet();

    private static final String             STRING_ATTR_ACCESS_METHOD_NAME        = "getStringAttribute";

    private static final String             DECIMAL_ATTR_ACCESS_METHOD_NAME       = "getDecimalAttribute";

    private static final String             DATE_ATTR_ACCESS_METHOD_NAME          = "getDateTimeAttributes";

    private static final String             BLOB_ATTR_ACCESS_METHOD_NAME          = "getBlobAttribute";

    public EntityExtendedAttrDescriptor() {}

    public EntityExtendedAttrDescriptor(String name, EntityAttrType type) {
        this.name = name;
        this.type = type;
    }

    public String getAccessMethodName(){
        if (accessMethodName == null) {
            switch (type) {
            case DATE:
                return DATE_ATTR_ACCESS_METHOD_NAME;
            case DECIMAL:
                return DECIMAL_ATTR_ACCESS_METHOD_NAME;
            case MONEY:
                return DECIMAL_ATTR_ACCESS_METHOD_NAME;
            case IMAGE:
                return BLOB_ATTR_ACCESS_METHOD_NAME;
            default:
                return STRING_ATTR_ACCESS_METHOD_NAME;
            }
        } else {
            return accessMethodName;
        }
    }

    public void setAccessMethodName(String accessMethodName){
        this.accessMethodName = accessMethodName;
    }

    @Override
    public String getName(){
        return this.name;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public EntityAttrType getType(){
        return type;
    }

    public void setType(EntityAttrType type){
        this.type = type;
    }

    public String getEntityName(){
        return entityName;
    }
    
    public void setEntityName(String entityName){
        this.entityName = entityName;
    }

    public EntityExtendedAttrConstraintSet getConstraintSet(){
        return constraintSet;
    }

    public void setConstraintSet(EntityExtendedAttrConstraintSet constraintSet){
        this.constraintSet = constraintSet;
    }

    public String getDisplay(){
        return display;
    }

    public void setDisplay(String display){
        this.display = display;
    }

    @Override
    public Object accessValue(Object entity){
        Assert.notNull(entity);
        Method method = ReflectionUtils.findMethod(entity.getClass(), this.getAccessMethodName(), String.class);
        return ReflectionUtils.invokeMethod(method, entity);
    }
    
}

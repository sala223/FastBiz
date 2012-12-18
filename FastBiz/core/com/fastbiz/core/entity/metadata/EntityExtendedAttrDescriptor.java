package com.fastbiz.core.entity.metadata;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.QueryHint;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import com.fastbiz.core.bootstrap.service.persistence.EmbeddedNullAllowedCustomizer;
import org.eclipse.persistence.annotations.Customizer;
import org.eclipse.persistence.annotations.Index;
import org.eclipse.persistence.config.QueryHints;

@XmlRootElement
@Entity
@Customizer(EmbeddedNullAllowedCustomizer.class)
@Table(name = "ENTITY_EXT_ATTR")
@NamedQueries({
                @NamedQuery(name = EntityExtendedAttrDescriptor.NQ_FIND_EXTENDED_ATTRS_BY_ENTITY_NAME, query = "SELECT e FROM EntityExtendedAttrDescriptor e where e.entityName=:ENTITY_NAME", hints = { @QueryHint(name = QueryHints.LEFT_FETCH, value = "e.constraints") }),
                @NamedQuery(name = EntityExtendedAttrDescriptor.NQ_FIND_EXTENDED_ATTR_BY_ATTR_NAME, query = "SELECT e FROM EntityExtendedAttrDescriptor e where e.entityName=:ENTITY_NAME and e.name=:ATTR_NAME", hints = { @QueryHint(name = QueryHints.LEFT_FETCH, value = "e.constraints") }) })
public class EntityExtendedAttrDescriptor implements EntityAttrDescriptor{

    public static final String                 NQ_FIND_EXTENDED_ATTRS_BY_ENTITY_NAME = "findAttrsByEntityName";

    public static final String                 NQ_FIND_EXTENDED_ATTR_BY_ATTR_NAME    = "findAttrByAttrName";

    private static final String                STRING_ATTR_ACCESS_METHOD_NAME        = "getStringAttribute";

    private static final String                DECIMAL_ATTR_ACCESS_METHOD_NAME       = "getDecimalAttribute";

    private static final String                DATE_ATTR_ACCESS_METHOD_NAME          = "getDateTimeAttributes";

    private static final String                BLOB_ATTR_ACCESS_METHOD_NAME          = "getBlobAttribute";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "EXT_ATTR_SEQ")
    @SequenceGenerator(allocationSize = 1, name = "EXT_ATTR_SEQ", sequenceName = "EXT_ATTR_SEQ")
    @Column(name = "ID")
    private long                               id;

    @Column(length = 56)
    @Index
    private String                             entityName;

    @Column(length = 56)
    private String                             name;

    @Column(length = 56)
    private String                             display;

    @Transient
    private String                             accessMethodName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EntityAttrType                     type;

    @Column(length = 256)
    private String                             description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ENTITY_EXT_ATTR_CONSTRAINT", joinColumns = @JoinColumn(name = "EXT_ATTR_ID"))
    @Column(name = "CONSTRAINT")
    private List<EntityExtendedAttrConstraint> constraints;

    public EntityExtendedAttrDescriptor() {
        this.constraints = new ArrayList<EntityExtendedAttrConstraint>();
    }

    public EntityExtendedAttrDescriptor(String name, EntityAttrType type) {
        this.constraints = new ArrayList<EntityExtendedAttrConstraint>();
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

    public long getId(){
        return id;
    }

    public void setId(long id){
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

    public String getDisplay(){
        return display;
    }

    public void setDisplay(String display){
        this.display = display;
    }

    public List<EntityExtendedAttrConstraint> getConstraints(){
        return Collections.unmodifiableList(constraints);
    }

    public void setConstraints(List<EntityExtendedAttrConstraint> constraints){
        this.constraints = constraints;
    }

    public void addConstraint(EntityExtendedAttrConstraint constraint){
        constraints.add(constraint);
    }

    public int getConstraintSize(){
        return constraints.size();
    }

    @Override
    public Object accessValue(Object entity){
        Assert.notNull(entity);
        Method method = ReflectionUtils.findMethod(entity.getClass(), this.getAccessMethodName(), String.class);
        return ReflectionUtils.invokeMethod(method, entity);
    }
}

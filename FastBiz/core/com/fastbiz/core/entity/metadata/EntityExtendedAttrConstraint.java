package com.fastbiz.core.entity.metadata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
import javax.persistence.Table;

@Entity
@Table(name = "ENTITY_EXT_ATTR_CONSTRAINT")
public class EntityExtendedAttrConstraint{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "CONSTRAINT_ID")
    private int                              id;

    @Column(length = 256)
    private String                           checkType;

    @ElementCollection
    @CollectionTable(name = "EXTENDED_ATTR_CONSTRAINT_PARS", joinColumns = @JoinColumn(name = "PAR_ID"))
    @MapKey(name = "name")
    private Map<String, ConstraintParameter> parameters;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getCheckType(){
        return checkType;
    }

    public void setCheckType(String checkType){
        this.checkType = checkType;
    }

    public Map<String, ConstraintParameter> getParameters(){
        return parameters;
    }

    public void setParameters(Map<String, ConstraintParameter> parameters){
        this.parameters = parameters;
    }

    public void addParameter(String name, String value){
        if (this.parameters == null) {
            parameters = new HashMap<String, ConstraintParameter>();
        }
        parameters.put(name, new ConstraintParameter(name, value));
    }

    @Embeddable
    public static class ConstraintParameter implements Serializable{

        private static final long serialVersionUID = 1L;

        @Column(name = "name", length = 56)
        private String            name;

        @Column(name = "value", length = 128)
        private String            value;

        public ConstraintParameter() {}

        public ConstraintParameter(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName(){
            return name;
        }

        public void setName(String name){
            this.name = name;
        }

        public String getValue(){
            return value;
        }

        public void setValue(String value){
            this.value = value;
        }
    }
}

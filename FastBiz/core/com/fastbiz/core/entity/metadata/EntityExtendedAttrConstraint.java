package com.fastbiz.core.entity.metadata;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import com.fastbiz.core.entity.extension.service.ExtensionException;

@XmlRootElement
@Embeddable
public class EntityExtendedAttrConstraint implements Serializable{

    private static final long                serialVersionUID = 1L;

    @Column(length = 56)
    private String                           name;

    @Column(length = 128)
    private String                           description;

    @Column(length = 128)
    @Enumerated(EnumType.STRING)
    private ConstraintCheckType              checkType;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "name", column = @Column(name = "PARAMETER1")),
                    @AttributeOverride(name = "value", column = @Column(name = "VALUE1")) })
    private ConstraintParameter              parameter1;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "name", column = @Column(name = "PARAMETER2")),
                    @AttributeOverride(name = "value", column = @Column(name = "VALUE2")) })
    private ConstraintParameter              parameter2;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "name", column = @Column(name = "PARAMETER3")),
                    @AttributeOverride(name = "value", column = @Column(name = "VALUE3")) })
    private ConstraintParameter              parameter3;

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "name", column = @Column(name = "PARAMETER4")),
                    @AttributeOverride(name = "value", column = @Column(name = "VALUE4")) })
    private ConstraintParameter              parameter4;

    @Transient
    private Map<String, ConstraintParameter> parameters;

    public EntityExtendedAttrConstraint() {}

    public EntityExtendedAttrConstraint(ConstraintCheckType checkType) {
        this(null, checkType);
    }

    public EntityExtendedAttrConstraint(String name, ConstraintCheckType checkType) {
        this.name = name;
        this.checkType = checkType;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public ConstraintCheckType getCheckType(){
        return checkType;
    }

    public void setCheckType(ConstraintCheckType checkType){
        this.checkType = checkType;
    }

    public void addParameter(String name, String value){
        if (parameter1 == null) {
            parameter1 = new ConstraintParameter(name, value);
        } else if (parameter2 == null) {
            parameter2 = new ConstraintParameter(name, value);
        } else if (parameter3 == null) {
            parameter3 = new ConstraintParameter(name, value);
        } else if (parameter4 == null) {
            parameter4 = new ConstraintParameter(name, value);
        } else {
            throw ExtensionException.EntityAttributeConstraintParameterMaxLimitationException();
        }
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void addParameter(String name, int value){
        addParameter(name, Integer.toString(value));
    }

    public void addParameter(String name, float value){
        addParameter(name, Float.toString(value));
    }

    public void addParameter(String name, double value){
        addParameter(name, Double.toString(value));
    }

    public void addParameter(String name, long value){
        addParameter(name, Long.toString(value));
    }

    public Map<String, ConstraintParameter> getParameters(){
        if (parameters == null) {
            parameters = new HashMap<String, ConstraintParameter>();
            if (parameter1 != null) {
                parameters.put(parameter1.getName(), parameter1);
            }
            if (parameter2 != null) {
                parameters.put(parameter2.getName(), parameter2);
            }
            if (parameter3 != null) {
                parameters.put(parameter3.getName(), parameter3);
            }
            if (parameter4 != null) {
                parameters.put(parameter4.getName(), parameter4);
            }
        }
        return parameters;
    }

    @Embeddable
    public static class ConstraintParameter implements Serializable{

        private static final long serialVersionUID = 1L;

        @Column(name = "parameter", length = 56)
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

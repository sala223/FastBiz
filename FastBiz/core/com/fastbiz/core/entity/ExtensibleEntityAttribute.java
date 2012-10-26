package com.fastbiz.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import com.fastbiz.core.entity.metadata.EntityAttrType;

@Entity
public class ExtensibleEntityAttribute extends MultiTenantSupport{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int            id;

    @Column(length = 128)
    private String         entity;

    @Column(length = 128)
    private String         attributeName;

    @Column(length = 56)
    @Enumerated(EnumType.STRING)
    private EntityAttrType attributeType;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getEntity(){
        return entity;
    }

    public void setEntity(String entity){
        this.entity = entity;
    }

    public String getAttributeName(){
        return attributeName;
    }

    public void setAttributeName(String attributeName){
        this.attributeName = attributeName;
    }

    public EntityAttrType getAttributeType(){
        return attributeType;
    }

    public void setAttributeType(EntityAttrType attributeType){
        this.attributeType = attributeType;
    }
}

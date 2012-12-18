package com.fastbiz.core.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.CollectionTable;
import javax.persistence.JoinColumn;
import javax.persistence.ElementCollection;
import javax.persistence.MapKey;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.util.Assert;

@MappedSuperclass
public abstract class ExtensibleEntity extends MultiTenantSupport{

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "ENTITY_ID"))
    @MapKey(name = "attributeName")
    private Map<String, StringAttribute>   stringAttributes   = new HashMap<String, StringAttribute>();

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "ENTITY_ID"))
    @MapKey(name = "attributeName")
    private Map<String, DecimalAttribute>  decimalAttributes  = new HashMap<String, DecimalAttribute>();

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "ENTITY_ID"))
    @MapKey(name = "attributeName")
    @JsonProperty
    private Map<String, DateTimeAttribute> dateTimeAttributes = new HashMap<String, DateTimeAttribute>();

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "ID"))
    @MapKey(name = "attributeName")
    @JsonProperty
    private Map<String, BlobAttribute>     blobAttributes     = new HashMap<String, BlobAttribute>();

    public Attribute getAttribute(String name){
        Attribute attribute = stringAttributes.get(name);
        if (attribute == null) {
            attribute = decimalAttributes.get(name);
            if (attribute == null) {
                attribute = dateTimeAttributes.get(name);
            }
        }
        return attribute;
    }

    public boolean hasAttribute(String name){
        if (!stringAttributes.containsKey(name)) {
            if (!decimalAttributes.containsKey(name)) {
                if (!dateTimeAttributes.containsKey(name)) {
                    return false;
                }
            }
        }
        return true;
    }

    public StringAttribute getStringAttribute(String name){
        return stringAttributes.get(name);
    }

    public DecimalAttribute getDecimalAttribute(String name){
        return decimalAttributes.get(name);
    }

    public DateTimeAttribute getDateTimeAttribute(String name){
        return dateTimeAttributes.get(name);
    }

    public BlobAttribute getBlobAttribute(String name){
        return blobAttributes.get(name);
    }

    public void addStringAttribute(String attributeName, String value){
        Assert.notNull(attributeName);
        Assert.notNull(value);
        stringAttributes.put(attributeName, new StringAttribute(attributeName, value));
    }

    public void addDecimalAttribute(String attributeName, Number value){
        Assert.notNull(attributeName);
        Assert.notNull(value);
        decimalAttributes.put(attributeName, new DecimalAttribute(attributeName, value));
    }

    public void addDateTimeAttribute(String attributeName, Date value){
        Assert.notNull(attributeName);
        Assert.notNull(value);
        dateTimeAttributes.put(attributeName, new DateTimeAttribute(attributeName, value));
    }

    public void addBlobAttribute(String attributeName, byte[] value){
        Assert.notNull(attributeName);
        Assert.notNull(value);
        blobAttributes.put(attributeName, new BlobAttribute(attributeName, value));
    }

    @PrePersist
    protected void prePersist(){
        fillDefaultValue();
    }

    protected void fillDefaultValue(){}

    protected Map<String, StringAttribute> getStringAttributes(){
        return stringAttributes;
    }

    protected void setStringAttributes(Map<String, StringAttribute> stringAttributes){
        this.stringAttributes = stringAttributes;
    }

    protected Map<String, DecimalAttribute> getDecimalAttributes(){
        return decimalAttributes;
    }

    protected void setDecimalAttributes(Map<String, DecimalAttribute> decimalAttributes){
        this.decimalAttributes = decimalAttributes;
    }

    protected Map<String, DateTimeAttribute> getDateTimeAttributes(){
        return dateTimeAttributes;
    }

    protected void setDateTimeAttributes(Map<String, DateTimeAttribute> dateTimeAttributes){
        this.dateTimeAttributes = dateTimeAttributes;
    }
}

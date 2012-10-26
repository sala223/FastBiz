package com.fastbiz.core.web.ui.taglib;

import org.springframework.util.Assert;
import com.fastbiz.core.entity.metadata.EntityAttrDescriptor;

public abstract class DefaultBeanProperty implements BeanProperty{

    private EntityAttrDescriptor entityField;

    private Object      entity;

    public DefaultBeanProperty(EntityAttrDescriptor entityField, Object entity) {
        Assert.notNull(entityField);
        this.entityField = entityField;
        this.entity = entity;
    }

    @Override
    public String getName(){
        return entityField.getName();
    }

    @Override
    public Object getValue(){
        if (entity != null) {
            return entityField.accessValue(entity);
        }
        return null;
    }
}

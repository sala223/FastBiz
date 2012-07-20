package com.fastbiz.core.entity;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Transient;
import org.eclipse.persistence.annotations.VirtualAccessMethods;

@VirtualAccessMethods
@MappedSuperclass
public class ExtensibleEntity extends MultiTenantSupport{

    @Transient
    protected Map<String, Object> extensions = new HashMap<String, Object>();

    @SuppressWarnings("unchecked")
    public <T> T get(String name){
        return (T) extensions.get(name);
    }

    public Object set(String name, Object value){
        return extensions.put(name, value);
    }

    @PrePersist
    protected void prePersist(){
        fillDefaultValue();
    }

    protected void fillDefaultValue(){}
}

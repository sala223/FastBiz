package com.fastbiz.core.entity.extension.service.contract;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrDescriptor;

@WebService
public interface IEntityExtensionService{

    @WebMethod
    public abstract List<EntityExtendedAttrDescriptor> getExtendedAttributes(String entityName);

    @WebMethod
    public abstract EntityExtendedAttrDescriptor getExtendedAttribute(String entityName, String attribute);

    @WebMethod
    public abstract void addEntityAttributes(String entityName, EntityExtendedAttrDescriptor ... attrs);

    @WebMethod
    public abstract int deleteEntityAttributes(String entityName, String ... attributes);

    @WebMethod
    public abstract void updateEntityAttribute(EntityExtendedAttrDescriptor attr);

    @WebMethod
    public abstract void updateEntityAttributes(EntityExtendedAttrDescriptor ... attrs);
}
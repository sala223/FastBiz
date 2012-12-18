package com.fastbiz.core.entity.validation;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import com.fastbiz.common.utils.StringUtils;
import com.fastbiz.core.bootstrap.service.persistence.ProjectUtils;
import com.fastbiz.core.entity.extension.service.ExtensionException;
import com.fastbiz.core.entity.extension.service.contract.IEntityExtensionService;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrDescriptor;
import com.fastbiz.core.tenant.TenantHolder;
import com.fastbiz.core.tenant.TenantNotBoundException;

public class TenantEntityExtendedAttrFactory implements EntityExtendedAttrFactory{

    @Autowired
    private IEntityExtensionService extensionService;

    public void setExtensionService(IEntityExtensionService extensionService){
        this.extensionService = extensionService;
    }

    protected EntityExtendedAttrDescriptor[] getTenantEntityExtendedAttrs(String tenantId, Class<?> entityType){
        ClassDescriptor descriptor = ProjectUtils.getClassDescriptor(entityType);
        if (descriptor == null) {
            throw ExtensionException.notAnEntityClassException(entityType);
        }
        return extensionService.getExtendedAttributes(descriptor.getAlias()).toArray(new EntityExtendedAttrDescriptor[0]);
    }

    @Override
    public EntityExtendedAttrDescriptor[] getEntityExtendedAttrs(Class<?> entityType){
        String tenantId = TenantHolder.getTenant();
        if (StringUtils.isNullOrEmpty(tenantId)) {
            String msg = "TenantId must be set in TenantHolder when getting entity extended attribute constraints";
            throw new TenantNotBoundException(msg);
        }
        return this.getTenantEntityExtendedAttrs(tenantId, entityType);
    }
}

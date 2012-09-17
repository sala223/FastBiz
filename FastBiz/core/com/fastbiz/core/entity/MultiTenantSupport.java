package com.fastbiz.core.entity;

import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import javax.persistence.MappedSuperclass;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@MappedSuperclass
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name = MultiTenantSupport.TENANT_COLUMN, length = 12, contextProperty = MultiTenantSupport.MULTITENANT_CONTEXT_PROPERTY)
public abstract class MultiTenantSupport{

    public static final String MULTITENANT_CONTEXT_PROPERTY = "fastbiz.tenant.id";

    public static final String SYSTEM_TENANT_PROPERTY       = "system.tenant.id";

    public static final String TENANT_COLUMN                = "TENANT_ID";

    public static final String SYSTEM_TENANT_COLUMN         = "SYSTE_TENANT_ID";
}

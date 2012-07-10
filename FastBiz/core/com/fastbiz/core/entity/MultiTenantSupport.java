package com.fastbiz.core.entity;

import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import org.eclipse.persistence.annotations.Index;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;

@MappedSuperclass
@Multitenant(SINGLE_TABLE)
@TenantDiscriminatorColumn(name = MultiTenantSupport.TENANT_COLUMN, length = 12, contextProperty = MultiTenantSupport.MULTITENANT_CONTEXT_PROPERTY)
public class MultiTenantSupport{

    public static final String MULTITENANT_CONTEXT_PROPERTY = "fastbiz.tenant.id";

    public static final String TENANT_COLUMN                = "TENANT_ID";

    @Column(name = TENANT_COLUMN, updatable = false, insertable = false)
    @Index
    protected String           tenantId;
}

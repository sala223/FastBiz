package com.fastbiz.solution.idm.entity;

import static org.eclipse.persistence.annotations.MultitenantType.SINGLE_TABLE;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantDiscriminatorColumn;
import org.eclipse.persistence.config.CacheIsolationType;
import com.fastbiz.core.entity.MultiTenantSupport;

@Entity
@Table(name = "SYSTEM_ROLE")
@Multitenant(SINGLE_TABLE)
@Cache(isolation=CacheIsolationType.SHARED)
@TenantDiscriminatorColumn(name = MultiTenantSupport.SYSTEM_TENANT_COLUMN, length = 12, contextProperty = MultiTenantSupport.SYSTEM_TENANT_PROPERTY)
public class SystemRole extends Role{ 

    private static final long serialVersionUID = 1L;
}

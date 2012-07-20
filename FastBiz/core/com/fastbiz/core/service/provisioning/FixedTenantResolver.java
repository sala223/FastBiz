package com.fastbiz.core.service.provisioning;

import com.fastbiz.core.tenant.TenantResolver;

public class FixedTenantResolver implements TenantResolver{

    private String tenantId;

    @Override
    public String getTenantId(){
        return tenantId;
    }

    public void setTenantId(String tenantId){
        this.tenantId = tenantId;
    }
}

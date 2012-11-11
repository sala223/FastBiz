package com.fastbiz.core.tenant;

import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;

public abstract class TenantHolder{

    private static final ThreadLocal<String> tenantHolder            = new NamedThreadLocal<String>("Tenant Context");

    private static final ThreadLocal<String> inheritableTenantHolder = new NamedInheritableThreadLocal<String>("Tenant context");

    public static void reset(){
        tenantHolder.remove();
        inheritableTenantHolder.remove();
    }

    public static String getTenant(){
        String tenantId = tenantHolder.get();
        if (tenantId == null) {
            tenantId = inheritableTenantHolder.get();
        }
        return tenantId;
    }

    public static void setTenant(String tenantId){
        setTenant(tenantId, false);
    }

    public static void setTenant(String tenantId, boolean inheritable){
        if (tenantId == null) {
            reset();
        } else {
            if (inheritable) {
                inheritableTenantHolder.set(tenantId);
                tenantHolder.remove();
            } else {
                tenantHolder.set(tenantId);
                inheritableTenantHolder.remove();
            }
        }
    }
}

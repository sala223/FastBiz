package com.fastbiz.core.tenant;

public class TenantHolder implements TenantResolver{

    private static final ThreadLocal<String> bindings = new ThreadLocal<String>();

    public static void clear(){
        bindings.remove();
    }

    public String getTenantId(){
        return bindings.get();
    }

    public static void setTenant(String tenantId){
        if (tenantId == null) {
            throw new IllegalArgumentException("TenantId cannot be null");
        }
        bindings.set(tenantId);
    }
}

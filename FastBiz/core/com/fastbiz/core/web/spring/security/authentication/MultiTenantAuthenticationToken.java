package com.fastbiz.core.web.spring.security.authentication;

import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class MultiTenantAuthenticationToken extends AbstractAuthenticationToken implements MultiTenantAuthentication{

    private static final long serialVersionUID = 1L;

    private Object            tenant;

    private Object            principal;

    private Object            credentials;

    public MultiTenantAuthenticationToken(Object tenant, Object principal, Object credentials) {
        super(null);
        this.tenant = tenant;
        this.principal = principal;
        this.credentials = credentials;
    }

    public MultiTenantAuthenticationToken(Object tenant, Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.tenant = tenant;
        this.principal = principal;
    }

    @Override
    public Object getCredentials(){
        return credentials;
    }

    @Override
    public Object getPrincipal(){
        return principal;
    }

    @Override
    public String getTenantId(){
        return tenant.toString();
    }

    @Override
    public void eraseCredentials(){
        super.eraseCredentials();
        credentials = null;
    }

    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof MultiTenantAuthenticationToken)) {
            return false;
        }
        MultiTenantAuthenticationToken test = (MultiTenantAuthenticationToken) obj;
        boolean isSuperEquals = super.equals(test);
        if (isSuperEquals) {
            if (this.getTenantId() == null && test.getTenantId() != null) {
                return false;
            }
            if (this.getTenantId() != null && !this.getTenantId().equals(test.getTenantId())) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode(){
        int code = super.hashCode();
        if (this.getTenantId() != null) {
            code ^= this.getDetails().hashCode();
        }
        return code;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("tenant:").append(this.getTenantId()).append("; ");
        return sb.toString();
    }
}

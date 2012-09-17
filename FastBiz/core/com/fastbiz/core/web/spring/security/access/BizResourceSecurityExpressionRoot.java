package com.fastbiz.core.web.spring.security.access;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

public class BizResourceSecurityExpressionRoot extends SecurityExpressionRoot{

    private BizResource resource;

    public BizResourceSecurityExpressionRoot(Authentication authentication, BizResource resource) {
        super(authentication);
        this.resource = resource;
    }

    public BizResource getResource(){
        return resource;
    }

    public boolean hasPermission(String permission){
        return super.hasPermission(authentication, null, permission);
    }
}

package com.fastbiz.core.web.spring.security.access;

import org.springframework.security.access.expression.AbstractSecurityExpressionHandler;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;

public class BizResourceSecurityExpressionHandler extends AbstractSecurityExpressionHandler<BizResource>{

    @Override
    protected SecurityExpressionRoot createSecurityExpressionRoot(Authentication authentication, BizResource resource){
        BizResourceSecurityExpressionRoot root = new BizResourceSecurityExpressionRoot(authentication, resource);
        root.setPermissionEvaluator(getPermissionEvaluator());
        return root;
    }
}

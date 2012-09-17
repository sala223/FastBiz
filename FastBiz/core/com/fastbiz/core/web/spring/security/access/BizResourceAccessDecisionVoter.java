package com.fastbiz.core.web.spring.security.access;

import java.util.Collection;
import org.springframework.expression.EvaluationContext;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.FilterInvocation;

public class BizResourceAccessDecisionVoter implements AccessDecisionVoter<BizResource>{

    private SecurityExpressionHandler<BizResource> expressionHandler = new BizResourceSecurityExpressionHandler();

    @Override
    public boolean supports(ConfigAttribute attribute){
        return attribute instanceof BizResourceExpressionConfigAttribute;
    }

    @Override
    public boolean supports(Class<?> clazz){
        return clazz.isAssignableFrom(FilterInvocation.class);
    }

    @Override
    public int vote(Authentication authentication, BizResource resource, Collection<ConfigAttribute> attributes){
        assert authentication != null;
        assert resource != null;
        assert attributes != null;
        BizResourceExpressionConfigAttribute weca = findConfigAttribute(attributes);
        if (weca == null) {
            return ACCESS_ABSTAIN;
        }
        EvaluationContext ctx = expressionHandler.createEvaluationContext(authentication, resource);
        return ExpressionUtils.evaluateAsBoolean(weca.getAuthorizeExpression(), ctx) ? ACCESS_GRANTED : ACCESS_DENIED;
    }

    private BizResourceExpressionConfigAttribute findConfigAttribute(Collection<ConfigAttribute> attributes){
        for (ConfigAttribute attribute : attributes) {
            if (attribute instanceof BizResourceExpressionConfigAttribute) {
                return (BizResourceExpressionConfigAttribute) attribute;
            }
        }
        return null;
    }

    public void setExpressionHandler(SecurityExpressionHandler<BizResource> expressionHandler){
        this.expressionHandler = expressionHandler;
    }
}

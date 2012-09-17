package com.fastbiz.core.web.spring.security.access;

import org.springframework.expression.Expression;
import org.springframework.security.access.ConfigAttribute;

public class BizResourceExpressionConfigAttribute implements ConfigAttribute{

    private static final long serialVersionUID = 1L;

    private final Expression  authorizeExpression;

    public BizResourceExpressionConfigAttribute(Expression authorizeExpression) {
        this.authorizeExpression = authorizeExpression;
    }

    Expression getAuthorizeExpression(){
        return authorizeExpression;
    }

    public String getAttribute(){
        return null;
    }

    @Override
    public String toString(){
        return authorizeExpression.getExpressionString();
    }
}

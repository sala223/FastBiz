package com.fastbiz.wms.web.service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.fastbiz.core.web.spring.security.access.BizResource;
import com.fastbiz.core.web.spring.security.access.BizResourceExpressionConfigAttribute;
import com.fastbiz.solution.idm.exception.AuthenticationException;
import com.fastbiz.wms.web.model.ui.FunctionGroup;

@Service("functionGroupService")
public class DefaultFunctionGroupService implements IFunctionGroupService, MessageSourceAware{

    @Autowired
    private FunctionGroupSource                    source;

    private boolean                                enableAuthorization;

    private AccessDecisionVoter<BizResource>       accessDecisionVoter;

    private SecurityExpressionHandler<BizResource> securityExpressionHandler;

    private MessageSource                          messageSource;

    private static final Logger                    LOG = LoggerFactory.getLogger(DefaultFunctionGroupService.class);

    public void setSource(FunctionGroupSource source){
        this.source = source;
    }

    public void setAccessDecisionVoter(AccessDecisionVoter<BizResource> accessDecisionVoter){
        this.accessDecisionVoter = accessDecisionVoter;
    }

    public void setEnableAuthorization(boolean enableAuthorization){
        this.enableAuthorization = enableAuthorization;
    }

    public void setSecurityExpressionHandler(SecurityExpressionHandler<BizResource> securityExpressionHandler){
        this.securityExpressionHandler = securityExpressionHandler;
    }

    @Override
    public FunctionGroup getSystemFunctionGroup(){
        FunctionGroup functionGroup = source.getFunctionGroup();
        if (enableAuthorization) {
            return applyAuthority(new FunctionGroup(functionGroup));
        } else {
            return functionGroup;
        }
    }

    protected FunctionGroup applyAuthority(FunctionGroup functionGroup){
        Authentication authentication = getSecurityContext().getAuthentication();
        if (authentication == null) {
            throw new AuthenticationException(AuthenticationException.ERROR_CODE_AUTHENTICATION_NOT_AUTHENTICATED);
        }
        ArrayList<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
        if (functionGroup.getAccessExpression() != null) {
            ExpressionParser expressionParser = securityExpressionHandler.getExpressionParser();
            Expression expression = expressionParser.parseExpression(functionGroup.getAccessExpression());
            BizResourceExpressionConfigAttribute configAttribute = new BizResourceExpressionConfigAttribute(expression);
            configAttributes.add(configAttribute);
            int isGranted = accessDecisionVoter.vote(authentication, functionGroup, configAttributes);
            if (isGranted != AccessDecisionVoter.ACCESS_GRANTED) {
                String fmt = "FunctionGroup {} is not granted access with expression {}";
                LOG.debug(fmt, functionGroup.getId(), functionGroup.getAccessExpression());
                return null;
            }
        }
        List<FunctionGroup> subs = functionGroup.getSubs();
        for (FunctionGroup sub : subs) {
            FunctionGroup fg = applyAuthority(sub);
            if (fg == null) {
                functionGroup.removeSubFunctionGroup(sub.getId());
            }
        }
        String key = functionGroup.getDisplay();
        functionGroup.setDisplay(messageSource.getMessage(key, null, LocaleContextHolder.getLocale()));
        return functionGroup;
    }

    protected SecurityContext getSecurityContext(){
        return SecurityContextHolder.getContext();
    }

    @Override
    public void setMessageSource(MessageSource messageSource){
        this.messageSource = messageSource;
    }
}

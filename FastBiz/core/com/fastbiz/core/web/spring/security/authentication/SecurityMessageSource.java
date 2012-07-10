package com.fastbiz.core.web.spring.security.authentication;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.core.SpringSecurityMessageSource;

public class SecurityMessageSource extends ReloadableResourceBundleMessageSource{

    private static final String SECURITY_DEFAULT_BASE_NAME = "classpath:message/SecurityMessages";

    public SecurityMessageSource() {
        setBasename(SECURITY_DEFAULT_BASE_NAME);
        this.setDefaultEncoding("utf-8");
        this.setParentMessageSource(new SpringSecurityMessageSource());
    }

    public static MessageSourceAccessor getAccessor(){
        return new MessageSourceAccessor(new SecurityMessageSource());
    }
}

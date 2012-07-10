package com.fastbiz.core.validation.message;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

class DefaultValidationMessageSource extends ReloadableResourceBundleMessageSource{

    private static final String VALIDATION_DEFAULT_BASE_NAME = "classpath:message/ValidationMessages";

    public DefaultValidationMessageSource() {
        this.setUseCodeAsDefaultMessage(true);
        this.setAlwaysUseMessageFormat(false);
        this.setCacheSeconds(-1);
        setBasename(VALIDATION_DEFAULT_BASE_NAME);
    }

    public static MessageSourceAccessor getAccessor(){
        return new MessageSourceAccessor(new DefaultValidationMessageSource());
    }
}

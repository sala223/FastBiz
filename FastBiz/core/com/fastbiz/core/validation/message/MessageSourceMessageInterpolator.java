package com.fastbiz.core.validation.message;

import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.MessageInterpolator;
import javax.validation.Payload;
import javax.validation.metadata.ConstraintDescriptor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import com.fastbiz.core.message.SolutionMessageSourceCreator;
import com.fastbiz.core.solution.SID;
import com.fastbiz.core.validation.SIDPayload;

public class MessageSourceMessageInterpolator implements MessageInterpolator{

    private static final String                   SOLUTION_VALIDATION_MESSAGES_BASENAME = "ValidationMessages";

    private static final Pattern                  messagePattern                        = Pattern.compile("\\{([\\w\\.]+)\\}");

    private MessageSource                         defaultMessageSource                  = new DefaultValidationMessageSource();

    private static final String                   defaultEncoding                       = "UTF-8";

    private SolutionMessageSourceCreator          messageSourceCreator;

    private Map<String, MessageSource>            messageSources                        = new ConcurrentHashMap<String, MessageSource>();

    private Map<Class<? extends Payload>, String> sidCache                              = new WeakHashMap<Class<? extends Payload>, String>();

    private String messageBaseName = SOLUTION_VALIDATION_MESSAGES_BASENAME;
    
    public MessageSourceMessageInterpolator(SolutionMessageSourceCreator messageSourceCreator) {
        setMessageSourceCreator(messageSourceCreator);
    }

    public void setMessageSourceCreator(SolutionMessageSourceCreator messageSourceCreator){
        this.messageSourceCreator = messageSourceCreator;
        this.messageSourceCreator.setDefaultEncoding(defaultEncoding);
    }

    private String replace(String sid, String message, Map<String, Object> parameters, Locale locale){
        Matcher matcher = messagePattern.matcher(message);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, resolveParameter(sid, matcher.group(1), parameters, locale));
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private String resolveParameter(String sid, String token, Map<String, Object> parameters, Locale locale){
        Object variable = parameters.get(token);
        if (variable != null) {
            return variable.toString();
        }
        MessageSource messageSource = getMessageSource(sid);
        StringBuffer buffer = new StringBuffer();
        String message = null;
        try {
            message = messageSource != null ? messageSource.getMessage(token, null, locale) : null;
        } catch (MissingResourceException ex) {}
        if (messageSource == null) {
            try {
                message = defaultMessageSource.getMessage(token, null, locale);
            } catch (MissingResourceException ex) {
                buffer.append("{").append(token).append('}');
            }
        }
        if (message != null) {
            buffer.append(replace(sid, message, parameters, locale));
        }
        return buffer.toString();
    }

    @Override
    public String interpolate(String message, Context context){
        return interpolate(message, context, LocaleContextHolder.getLocale());
    }

    @Override
    public String interpolate(String message, Context context, Locale locale){
        ConstraintDescriptor<?> descriptor = context.getConstraintDescriptor();
        String sid = traitSolutionId(context);
        return replace(sid, message, descriptor.getAttributes(), locale);
    }

    
    public void setMessageBaseName(String messageBaseName){
        this.messageBaseName = messageBaseName;
    }

    protected MessageSource getMessageSource(String sid){
        if (sid == null) {
            return defaultMessageSource;
        }
        MessageSource messageSource = messageSources.get(sid);
        if (messageSource == null) {
            String[] basenames = { messageBaseName };
            messageSource = messageSourceCreator.createMessageSource(sid, basenames);
        }
        if (messageSource == null) {
            messageSource = defaultMessageSource;
        }
        messageSources.put(sid, messageSource);
        return messageSource;
    }

    protected String traitSolutionId(Context context){
        Set<Class<? extends Payload>> payloads = context.getConstraintDescriptor().getPayload();
        if (payloads == null || payloads.size() == 0) {
            return null;
        }
        for (Class<? extends Payload> payloadClass : payloads) {
            if (ClassUtils.isAssignable(SIDPayload.class, payloadClass)) {
                String sid = sidCache.get(payloadClass);
                if (sid == null) {
                    SID sidAnnotation = AnnotationUtils.findAnnotation(payloadClass, SID.class);
                    if (sidAnnotation == null) {
                        return null;
                    } else {
                        sidCache.put(payloadClass, sidAnnotation.value());
                        return sidAnnotation.value();
                    }
                }
            }
        }
        return null;
    }
}
package com.fastbiz.core.locale;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

public class LocaleContextHolderFilter extends OncePerRequestFilter{

    private LocaleResolver localeResolver;

    public void setLocaleResolver(LocaleResolver localeResolver){
        this.localeResolver = localeResolver;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException{
        Locale locale = null;
        if (localeResolver != null) {
            locale = localeResolver.resolveLocale(request);
        } else {
            locale = RequestContextUtils.getLocale(request);
        }
        if (locale != null) {
            LocaleContextHolder.setLocale(locale);
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            LocaleContextHolder.resetLocaleContext();
        }
    }
}

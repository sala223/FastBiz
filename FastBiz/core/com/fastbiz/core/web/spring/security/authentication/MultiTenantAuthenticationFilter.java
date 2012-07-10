package com.fastbiz.core.web.spring.security.authentication;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class MultiTenantAuthenticationFilter extends AbstractAuthenticationProcessingFilter{

    public static final String DEFAULT_FILTER_PROCESS_URL = "/login/authentication";

    public static final String SECURITY_AUTH_USERNAME_KEY = "f_username";

    public static final String SECURITY_AUTH_PASSWORD_KEY = "f_password";

    public static final String SECURITY_AUTH_TENANT_KEY   = "f_tenant";

    protected MultiTenantAuthenticationFilter() {
        this(DEFAULT_FILTER_PROCESS_URL);
        
    }

    protected MultiTenantAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        this.messages = SecurityMessageSource.getAccessor();
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
                    throws AuthenticationException, IOException, ServletException{
        String username = obtainUserName(request);
        String password = obtainUserPassword(request);
        String tenant = obtainTenant(request);
        if (tenant == null) {
            tenant = "";
        }
        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        username = username.trim();
        MultiTenantAuthenticationToken authRequest = new MultiTenantAuthenticationToken(tenant, username, password);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainUserName(HttpServletRequest request){
        return request.getParameter(SECURITY_AUTH_USERNAME_KEY);
    }

    protected String obtainUserPassword(HttpServletRequest request){
        return request.getParameter(SECURITY_AUTH_PASSWORD_KEY);
    }

    protected String obtainTenant(HttpServletRequest request){
        return request.getParameter(SECURITY_AUTH_TENANT_KEY);
    }

    @Override
    public void setMessageSource(MessageSource messageSource){}
}

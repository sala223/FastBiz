package com.fastbiz.core.web.security.csrf;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

public class CSRFTokenCheckFilter extends OncePerRequestFilter{

    @Autowired
    private CSRFTokenService csrfTokenService;

    public CSRFTokenCheckFilter() {}

    public CSRFTokenCheckFilter(CSRFTokenService csrfTokenService) {
        this.setCSRFTokenService(csrfTokenService);
    }

    public void setCSRFTokenService(CSRFTokenService csrfTokenService){
        this.csrfTokenService = csrfTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException{
        String method = request.getMethod() == null ? "" : request.getMethod().toUpperCase();
        if (csrfTokenService.getMethodsToCheck().contains(method) && !csrfTokenService.acceptsTokenIn(request)) {
            response.addHeader("X-FastBiz-Invalid-CSRFToken", Boolean.toString(true));
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}

package com.fastbiz.core.tenant;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fastbiz.core.web.spring.security.authentication.MultiTenantAuthenticationToken;

public class TenantContextBindFilter extends OncePerRequestFilter{

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication instanceof MultiTenantAuthenticationToken) {
            MultiTenantAuthenticationToken authenticationToken = (MultiTenantAuthenticationToken) authentication;
            TenantHolder.setTenant(authenticationToken.getTenantId());
        }
        filterChain.doFilter(request, response);
    }
}

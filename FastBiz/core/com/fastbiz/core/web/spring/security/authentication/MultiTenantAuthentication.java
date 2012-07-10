package com.fastbiz.core.web.spring.security.authentication;

import org.springframework.security.core.Authentication;

public interface MultiTenantAuthentication extends Authentication{

    public String getTenantId(); 
}

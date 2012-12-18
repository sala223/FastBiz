package com.fastbiz.core.web.security.csrf;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

public interface CSRFTokenService{

    public final static String       TOKEN_PARAMETER_NAME = "_tk";

    public final static String       TOKEN_ATTRIBUTE_NAME = "com.fastbiz.web.security.CSRFToken";

    public String generateToken();

    public String getTokenFromSession(HttpServletRequest request);

    public boolean acceptsTokenIn(HttpServletRequest request);
    
    public List<String> getMethodsToCheck();
}

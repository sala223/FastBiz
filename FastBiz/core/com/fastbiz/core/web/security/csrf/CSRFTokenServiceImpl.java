package com.fastbiz.core.web.security.csrf;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.fastbiz.common.utils.StringUtils;

@Service("csrfTokenService")
public class CSRFTokenServiceImpl implements CSRFTokenService{

    private final SecureRandom random         = new SecureRandom();

    public List<String>        methodsToCheck = Collections.unmodifiableList(Arrays.asList("POST", "PUT", "DELETE"));

    @Override
    public String generateToken(){
        final byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.encodeBase64URLSafeString(bytes);
    }

    @Override
    public String getTokenFromSession(final HttpServletRequest request){
        return request.getUserPrincipal() == null ? null : this.getTokenFromSessionImpl(request.getSession(false));
    }

    private String getTokenFromSessionImpl(HttpSession session){
        String token = null;
        if (session != null) {
            token = (String) session.getAttribute(TOKEN_ATTRIBUTE_NAME);
            if (StringUtils.isNullOrEmpty(token))
                session.setAttribute(TOKEN_ATTRIBUTE_NAME, (token = generateToken()));
        }
        return token;
    }

    @Override
    public boolean acceptsTokenIn(HttpServletRequest request){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean rv = false;
        if (principal == null)
            rv = true;
        else {
            HttpSession session = request.getSession(false);
            rv = session != null && this.getTokenFromSessionImpl(session).equals(request.getParameter(TOKEN_PARAMETER_NAME));
        }
        return rv;
    }

    @Override
    public List<String> getMethodsToCheck(){
        return methodsToCheck;
    }

    public void setMethodsToCheck(List<String> methodsToCheck){
        this.methodsToCheck = methodsToCheck;
    }
}

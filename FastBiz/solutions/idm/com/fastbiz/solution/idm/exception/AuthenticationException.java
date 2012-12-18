package com.fastbiz.solution.idm.exception;

public class AuthenticationException extends IDMException{

    private static final long  serialVersionUID                              = 1L;

    public static String       AUTHENTICATION_SERVICE                        = "AuthenticationService";

    public static final String ERROR_CODE_AUTHENTICATION_USER_NAME_NOT_FOUND = "AUTHENTICATION_USER_NAME_NOT_FOUND";

    public static final String ERROR_CODE_AUTHENTICATION_BAD_CREDENTIAL      = "AUTHENTICATION_BAD_CREDENTIAL";

    public static final String ERROR_CODE_AUTHENTICATION_NOT_AUTHENTICATED   = "AUTHENTICATION_NOT_AUTHENTICATED";

    public AuthenticationException(String errorCode) {
        super(AUTHENTICATION_SERVICE, errorCode);
    }

    public AuthenticationException(String errorCode, String message, Object ... args) {
        super(AUTHENTICATION_SERVICE, errorCode, message, args);
    }

    public static AuthenticationException userNameNotFound(){
        return new AuthenticationException(ERROR_CODE_AUTHENTICATION_USER_NAME_NOT_FOUND);
    }

    public static AuthenticationException badCredential(){
        return new AuthenticationException(ERROR_CODE_AUTHENTICATION_BAD_CREDENTIAL);
    }

    public static AuthenticationException notAuthenticated(){
        return new AuthenticationException(ERROR_CODE_AUTHENTICATION_NOT_AUTHENTICATED);
    }
}

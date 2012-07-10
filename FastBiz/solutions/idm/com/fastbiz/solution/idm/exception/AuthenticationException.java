package com.fastbiz.solution.idm.exception;

public class AuthenticationException extends IDMException{

    private static final long serialVersionUID                   = 1L;

    public static String      AUTHENTICATION_SERVICE             = "AuthenticationService";

    public static int         AUTHENTICATION_USER_NAME_NOT_FOUND = 1000;

    public static int         AUTHENTICATION_BAD_CREDENTIAL      = 1001;

    public AuthenticationException(int errorCode) {
        super(AUTHENTICATION_SERVICE, errorCode);
    }

    public AuthenticationException(int errorCode, String message, Object ... args) {
        super(AUTHENTICATION_SERVICE, errorCode, message, args);
    }

    public static AuthenticationException userNameNotFound(){
        return new AuthenticationException(AUTHENTICATION_USER_NAME_NOT_FOUND);
    }

    public static AuthenticationException badCredential(){
        return new AuthenticationException(AUTHENTICATION_BAD_CREDENTIAL);
    }
}

package com.fastbiz.common.utils;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import com.fastbiz.core.bootstrap.service.jndi.JNDIException;

public class JNDIUtils{

    public static InitialContext getInitialContext(){
        try {
            return new InitialContext();
        } catch (NamingException ex) {
            throw new JNDIException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T lookup(String name){
        try {
            InitialContext initialContext = new InitialContext();
            return (T) initialContext.lookup(name);
        } catch (NamingException ex) {
            throw new JNDIException(ex);
        }
    }
}

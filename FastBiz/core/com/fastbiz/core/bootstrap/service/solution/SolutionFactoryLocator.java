package com.fastbiz.core.bootstrap.service.solution;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import com.fastbiz.core.bootstrap.service.jndi.JNDIException;
import com.fastbiz.core.solution.SolutionFactory;

public class SolutionFactoryLocator{

    public static SolutionFactory getSolutionFactory(){
        try {
            InitialContext context = new InitialContext();
            return (SolutionFactory) context.lookup(SolutionPackageService.SOLUTION_FACTORY_JNDI);
        } catch (NamingException ex) {
            throw new JNDIException(ex);
        }
    }
}

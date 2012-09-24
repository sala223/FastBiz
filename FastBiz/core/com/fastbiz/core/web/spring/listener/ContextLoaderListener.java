package com.fastbiz.core.web.spring.listener;

import javax.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import com.fastbiz.core.bootstrap.service.solution.SolutionFactoryLocator;
import com.fastbiz.core.solution.Solution;
import com.fastbiz.core.solution.StandardSolution;

public class ContextLoaderListener extends org.springframework.web.context.ContextLoaderListener{

    private static final String SOLUTION_ID_PARAMETER = "solutionId";

    protected ApplicationContext loadParentContext(ServletContext servletContext){
        String solutionId = servletContext.getInitParameter(SOLUTION_ID_PARAMETER);
        if (StringUtils.isEmpty(solutionId)) {
            servletContext.log("solutionId is not specified in servlet context init parameter.");
            return null;
        }
        Solution solution = SolutionFactoryLocator.getSolutionFactory().getSolution(solutionId);
        if (solution == null) {
            servletContext.log(String.format("Invalid solution id %s, it is not managed yet.", solutionId));
            return null;
        } else {
            boolean hasCXF = solution.containsBean("cxf");
            if (hasCXF) {
                servletContext.addServlet("CXFServlet", "org.apache.cxf.transport.servlet.CXFServlet");
                servletContext.getServletRegistration("CXFServlet").addMapping("/wsapi/*");
            }
            return ((StandardSolution) solution).unwrap(ApplicationContext.class);
        }
    }
}

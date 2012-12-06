package com.fastbiz.core.web.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.context.ApplicationContext;
import com.fastbiz.common.utils.StringUtils;
import com.fastbiz.core.bootstrap.service.solution.SolutionFactoryLocator;
import com.fastbiz.core.solution.Solution;
import com.fastbiz.core.solution.StandardSolution;

public class ContextLoaderListener extends org.springframework.web.context.ContextLoaderListener{

    private static final String SOLUTION_ID_PARAMETER    = "solution-id";

    private static final String CXF_URL_PREFIX_PARAMETER = "cxf-url-prefix";

    private static final String DEFAULT_CXF_URL_PREFIX   = "/api/*";

    private static final String SOLUTION_TOOT_ATTRIBUTE  = "com.fastbiz.SOLUTION_TOOT_ATTRIBUTE";

    protected ApplicationContext loadParentContext(ServletContext servletContext){
        String solutionId = servletContext.getInitParameter(SOLUTION_ID_PARAMETER);
        if (StringUtils.isNullOrEmpty(solutionId)) {
            servletContext.log("solutionId is not specified in servlet context init parameter.");
            return null;
        }
        Solution solution = SolutionFactoryLocator.getSolutionFactory().getSolution(solutionId);
        if (solution == null) {
            servletContext.log(String.format("Invalid solution id %s, it is not managed yet.", solutionId));
            return null;
        } else {
            servletContext.setAttribute(SOLUTION_TOOT_ATTRIBUTE, solution);
            return ((StandardSolution) solution).getApplicationContext();
        }
    }

    @Override
    public void contextInitialized(ServletContextEvent event){
        super.contextInitialized(event);
        ServletContext servletContext = event.getServletContext();
        servletContext.addServlet("CXFServlet", CXFServlet.class);
        String cxfUrlPrefix = servletContext.getInitParameter(CXF_URL_PREFIX_PARAMETER);
        cxfUrlPrefix = cxfUrlPrefix == null ? DEFAULT_CXF_URL_PREFIX : cxfUrlPrefix;
        servletContext.getServletRegistration("CXFServlet").addMapping(cxfUrlPrefix);
    }
}

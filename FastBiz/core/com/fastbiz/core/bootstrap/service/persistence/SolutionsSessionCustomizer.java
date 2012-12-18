package com.fastbiz.core.bootstrap.service.persistence;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.Session;

public class SolutionsSessionCustomizer implements SessionCustomizer{

    @Override
    public void customize(Session session) throws Exception{
        ProjectUtils.setProject(session.getProject());
    }
}

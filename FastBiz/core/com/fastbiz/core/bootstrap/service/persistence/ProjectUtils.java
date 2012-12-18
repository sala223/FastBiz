package com.fastbiz.core.bootstrap.service.persistence;

import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.sessions.Project;
import com.fastbiz.common.utils.Assert;

public abstract class ProjectUtils{

    private static Project project;

    public static void setProject(Project project){
        ProjectUtils.project = project;
    }

    public static ClassDescriptor getClassDescriptor(Class<?> entityType){
        checkProject();
        Assert.notNull(entityType);
        return project.getClassDescriptor(entityType);
    }

    private static void checkProject(){
        Assert.notNull(project, "Project must be set before any method call");
    }
}

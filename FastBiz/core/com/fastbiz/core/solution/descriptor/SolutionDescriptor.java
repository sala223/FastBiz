package com.fastbiz.core.solution.descriptor;

public interface SolutionDescriptor{

    String getSolutionId();

    String getSolutionPath();

    public String getEntityXmlORMappingFilePath();

    public String[] getBeanConfigurationFilesPathes();
}

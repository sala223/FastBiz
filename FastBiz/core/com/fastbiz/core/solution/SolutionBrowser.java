package com.fastbiz.core.solution;

import java.io.File;
import com.fastbiz.core.solution.descriptor.SolutionDescriptor;

public interface SolutionBrowser{

    boolean hasSolution(String solutionId);

    String[] getSolutionIds();

    String getSolutionPath(String solutionId);

    File getEntityXmlORMappingFile(String solutionId);

    SolutionDescriptor getSolutionDescriptor(String solutionId);
}

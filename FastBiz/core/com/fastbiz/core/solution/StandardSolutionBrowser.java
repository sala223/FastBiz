package com.fastbiz.core.solution;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import com.fastbiz.common.utils.FileUtils;
import com.fastbiz.core.bootstrap.EnvironmentConfigration;
import com.fastbiz.core.solution.descriptor.SolutionDescriptor;
import com.fastbiz.core.solution.descriptor.StandardSolutionDescriptor;

public class StandardSolutionBrowser implements SolutionBrowser{

    private static final String             DEFAULT_SOLUTIONS_DIR = "runtime/solutions";

    private Map<String, SolutionDescriptor> solutions             = new HashMap<String, SolutionDescriptor>();

    public StandardSolutionBrowser() {
        this(null);
    }

    public StandardSolutionBrowser(String solutionsDirPath) {
        if (solutionsDirPath == null) {
            solutionsDirPath = (String) EnvironmentConfigration.getAttribute(EnvironmentConfigration.SOLUTIONS_DIR_ATTR);
            if (solutionsDirPath == null) {
                solutionsDirPath = FileUtils.newFilePath(EnvironmentConfigration.getBaseDir(), DEFAULT_SOLUTIONS_DIR);
            }
        }
        File solutionsDir = new File(solutionsDirPath);
        File[] solutionDirs = null;
        if (solutionsDir.exists()) {
            solutionDirs = FileUtils.listDirectories(solutionsDir);
        } else {
            solutionDirs = new File[0];
        }
        for (int i = 0; i < solutionDirs.length; ++i) {
            solutions.put(solutionDirs[i].getName(), new StandardSolutionDescriptor(solutionDirs[i]));
        }
    }

    @Override
    public String[] getSolutionIds(){
        return solutions.keySet().toArray(new String[0]);
    }

    @Override
    public String getSolutionPath(String solutionId){
        SolutionDescriptor desc = checkSolutionIdExistence(solutionId);
        return desc.getSolutionPath();
    }

    protected SolutionDescriptor checkSolutionIdExistence(String solutionId){
        SolutionDescriptor desc = solutions.get(solutionId);
        if (desc == null) {
            throw SolutionException.SolutionDoesNotExist(solutionId);
        }
        return desc;
    }

    @Override
    public boolean hasSolution(String solutionId){
        return solutions.get(solutionId) != null;
    }

    @Override
    public SolutionDescriptor getSolutionDescriptor(String solutionId){
        return solutions.get(solutionId);
    }

    @Override
    public File getEntityXmlORMappingFile(String solutionId){
        SolutionDescriptor desc = solutions.get(solutionId);
        if (desc == null) {
            throw SolutionException.SolutionDoesNotExist(solutionId);
        }
        return new File(desc.getEntityXmlORMappingFilePath());
    }
}

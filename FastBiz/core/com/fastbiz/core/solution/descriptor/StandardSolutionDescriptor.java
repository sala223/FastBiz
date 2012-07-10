package com.fastbiz.core.solution.descriptor;

import java.io.File;
import com.fastbiz.common.utils.Assert;

public class StandardSolutionDescriptor implements SolutionDescriptor{

    private static final String DEFAULT_OR_MAPPING_FILE_NAME   = "orm.xml";

    private static final String DEFAULT_BEANS_CONFIG_FILE_NAME = "beans.xml";

    private static final String DEFAULT_CXF_CONFIG_FILE_NAME   = "cxf.xml";

    private File                directory;

    public StandardSolutionDescriptor(File solutionDirectory) {
        Assert.notNull(solutionDirectory, "solutionDirectory cannot be null");
        if (!solutionDirectory.isDirectory()) {
            throw new IllegalArgumentException("solutionDirectory must be an existing directory");
        }
        this.directory = solutionDirectory;
    }

    @Override
    public String getSolutionId(){
        return this.directory.getName();
    }

    @Override
    public String getSolutionPath(){
        return directory.getAbsolutePath();
    }

    @Override
    public String getEntityXmlORMappingFilePath(){
        return new File(directory, DEFAULT_OR_MAPPING_FILE_NAME).getAbsolutePath();
    }

    @Override
    public String[] getBeanConfigurationFilesPathes(){
        File cxfFile = new File(directory, DEFAULT_CXF_CONFIG_FILE_NAME);
        if (cxfFile.exists()) {
            return new String[] { new File(directory, DEFAULT_BEANS_CONFIG_FILE_NAME).getAbsolutePath(),
                            cxfFile.getAbsolutePath() };
        } else {
            return new String[] { new File(directory, DEFAULT_BEANS_CONFIG_FILE_NAME).getAbsolutePath()};
        }
    }
}

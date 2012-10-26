package com.fastbiz.core.solution.spring;

import java.io.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ContextResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import com.fastbiz.core.solution.SolutionDescriptorAware;
import com.fastbiz.core.solution.descriptor.SolutionDescriptor;

public class SolutionResoruceLoader extends FileSystemResourceLoader implements SolutionDescriptorAware{

    private SolutionDescriptor  solutionDescriptor;

    private static final Logger LOG = LoggerFactory.getLogger(SolutionResoruceLoader.class);

    public SolutionResoruceLoader() {}

    public SolutionResoruceLoader(SolutionDescriptor solutionDescriptor) {
        setSolutionDescriptor(solutionDescriptor);
    }

    @Override
    public void setSolutionDescriptor(SolutionDescriptor solutionDescriptor){
        this.solutionDescriptor = solutionDescriptor;
    }

    @Override
    protected Resource getResourceByPath(String path){
        if (solutionDescriptor == null) {
            String message = "solution context is not set, please call setSolutionDescriptor to set solution context";
            throw new NullPointerException(message);
        }
        if (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        String sid = solutionDescriptor.getSolutionId();
        LOG.debug("Loading solution resource {} from solution {}", path, sid);
        return new SolutionResource(sid, solutionDescriptor.getSolutionPath(), path);
    }

    public static class SolutionResource extends FileSystemResource implements ContextResource{

        private String solutionId;

        private String resourceRelativePath;

        public SolutionResource(SolutionDescriptor solutionDescriptor, String resourceRelativePath) {
            super(solutionDescriptor.getSolutionPath() + resourceRelativePath);
            this.solutionId = solutionDescriptor.getSolutionId();
        }

        public SolutionResource(String solutionId, String solutionRootPath, String resourceRelativePath) {
            super(solutionRootPath + File.separator + resourceRelativePath);
            this.solutionId = solutionId;
        }

        @Override
        public String getPathWithinContext(){
            return resourceRelativePath;
        }

        public String getSolutionId(){
            return solutionId;
        }

        public String getDescription(){
            return "SID=[" + solutionId + "]" + "file [" + this.getPath() + "]";
        }
    }
}

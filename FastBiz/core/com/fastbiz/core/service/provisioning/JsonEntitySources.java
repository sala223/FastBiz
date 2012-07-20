package com.fastbiz.core.service.provisioning;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import com.fastbiz.common.utils.json.JsonUtils;
import com.fastbiz.core.solution.SolutionDescriptorAware;
import com.fastbiz.core.solution.descriptor.SolutionDescriptor;

public class JsonEntitySources implements EntitySources, SolutionDescriptorAware{

    private List<JsonFile>              files        = new ArrayList<JsonFile>();

    private SolutionDescriptor          solutionDescriptor;

    private List<EntityPostConstructor> postConstructors;

    private boolean                     isOpen;

    private int                         currentIndex = 0;

    public void reset(){
        isOpen = false;
        currentIndex = 0;
    }

    public void setJsonFiles(List<JsonFile> files){
        if (isOpen) {
            throw new IllegalStateException("This object is open to be read, call reset first before initialization");
        }
        Assert.notNull(files);
        this.files = files;
    }

    public void setPostConstructors(List<EntityPostConstructor> postConstructors){
        this.postConstructors = postConstructors;
    }

    @Override
    public List<EntityPostConstructor> getPostConstructors(){
        if(this.postConstructors != null){
            return Collections.unmodifiableList(postConstructors);
        }
        return null;
    }

    @Override
    public List<?> getEntitySet(){
        JsonFile jFile = files.get(currentIndex);
        currentIndex++;
        File file = new File(solutionDescriptor.getSolutionPath(), jFile.getFileName());
        return new JsonEntitySource(file, jFile.getEntityClassName()).getEntitySet();
    }

    @Override
    public boolean hasNext(){
        return files.size() > currentIndex;
    }

    @Override
    public void setSolutionDescriptor(SolutionDescriptor solutionDescriptor){
        this.solutionDescriptor = solutionDescriptor;
    }

    public static class JsonFile{

        private String fileName;

        private String entityClassName;

        JsonFile() {}

        public JsonFile(String fileName, String entityClassName) {
            this.fileName = fileName;
            this.entityClassName = entityClassName;
        }

        public String getFileName(){
            return fileName;
        }

        public void setFileName(String fileName){
            this.fileName = fileName;
        }

        public String getEntityClassName(){
            return entityClassName;
        }

        public void setEntityClassName(String entityClassName){
            this.entityClassName = entityClassName;
        }
    }

    static class JsonEntitySource implements EntitySource{

        private File   file;

        private String entityClassName;

        public JsonEntitySource(File file, String entityClassName) {
            this.file = file;
            this.entityClassName = entityClassName;
        }

        @Override
        public List<?> getEntitySet(){
            try {
                if (!file.exists()) {
                    throw new DataProvisioningException("Json file %s doesn't exist", file);
                }
                Class<?> entityClass = ClassUtils.forName(entityClassName, ClassUtils.getDefaultClassLoader());
                Object object = JsonUtils.readObject(file, entityClass);
                if (object.getClass().isArray()) {
                    Object[] objs = (Object[]) object;
                    return Arrays.asList(objs);
                } else if (object instanceof List<?>) {
                    return (List<?>) object;
                } else if (object instanceof Collection<?>) {
                    ArrayList<Object> list = new ArrayList<Object>();
                    list.addAll((Collection<?>) object);
                    return list;
                } else {
                    return Arrays.asList(object);
                }
            } catch (ClassNotFoundException ex) {
                throw new DataProvisioningException(ex, "Cannot find entity class %s for mapping", entityClassName);
            }
        }
    }
}

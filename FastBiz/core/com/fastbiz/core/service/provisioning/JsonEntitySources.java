package com.fastbiz.core.service.provisioning;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import com.fastbiz.common.utils.json.JsonUtils;

public class JsonEntitySources extends AbstractEntitySources{

    private List<JsonFile> files        = new ArrayList<JsonFile>();

    private boolean        isOpen;

    private int            currentIndex = 0;

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

    @Override
    public List<?> getEntitySet(){
        JsonFile jFile = files.get(currentIndex);
        currentIndex++;
        File file = getFileByFileName(jFile.getFileName());
        return new JsonEntitySource(file, jFile.getEntityClassName()).getEntitySet();
    }

    protected File getFileByFileName(String fileName){
        return new File(fileName);
    }

    @Override
    public boolean hasNext(){
        return files.size() > currentIndex;
    }

    @Override
    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{");
        for (JsonFile file : files) {
            buffer.append(file.toString() + ";");
        }
        buffer.append("}");
        return buffer.toString();
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

        @Override
        public String toString(){
            return "JsonFile [fileName=" + fileName + ", entityClassName=" + entityClassName + "]";
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

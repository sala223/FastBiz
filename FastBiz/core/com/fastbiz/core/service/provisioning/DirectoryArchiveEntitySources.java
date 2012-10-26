package com.fastbiz.core.service.provisioning;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import com.fastbiz.common.structure.OrderedProperties;
import com.fastbiz.common.utils.FileUtils;

public class DirectoryArchiveEntitySources extends JsonEntitySources implements InitializingBean, ApplicationContextAware{

    private File                directory;

    private String              ENTITY_SOURCES_CONF_FILE = "provisioning.properties";

    private static final String POST_CONSTRUCTOR_PREFIX  = "post-constructor";

    private ApplicationContext  applicationContext;

    private static final Logger log                      = LoggerFactory.getLogger(DirectoryArchiveEntitySources.class);

    public DirectoryArchiveEntitySources(File directory) {
        this.directory = directory;
    }

    public DirectoryArchiveEntitySources(ResourceLoader loader, String directory) {
        Resource resource = loader.getResource(directory);
        if (resource.exists()) {
            try {
                this.directory = resource.getFile();
            } catch (IOException ex) {
                throw new DataProvisioningException(ex);
            }
        }
    }

    protected File getFileByFileName(String fileName){
        return new File(directory, fileName);
    }

    public void initializeWithConfigurationFile(){
        if (directory == null) {
            log.warn("diretory is not set,assume empty entity sources");
            return;
        }
        String confFilePath = FileUtils.newFilePath(directory, ENTITY_SOURCES_CONF_FILE);
        File confFile = new File(confFilePath);
        if (confFile.isFile()) {
            Properties properties = new OrderedProperties();
            try {
                properties.load(new FileInputStream(confFile));
                List<EntityPostConstructor> postConstructors = new ArrayList<EntityPostConstructor>();
                List<JsonFile> files = new ArrayList<JsonFile>();
                Set<Object> keys = properties.keySet();
                for (Object object : keys) {
                    String key = (String) object;
                    String value = properties.getProperty(key);
                    if (key.startsWith(POST_CONSTRUCTOR_PREFIX)) {
                        if (applicationContext.containsBean(value)) {
                            Object bean = applicationContext.getBean(value);
                            if (bean instanceof EntityPostConstructor) {
                                postConstructors.add((EntityPostConstructor) bean);
                            } else {
                                String fmt = "The post constructor bean {} is not an instance of {}";
                                log.warn(fmt, value, EntityPostConstructor.class.getName());
                            }
                        } else {
                            String fmt = "The post constructor bean {} does not exist";
                            log.warn(fmt, value);
                        }
                    } else {
                        log.debug("List provisioning file {}", value);
                        files.add(new JsonFile(value, key));
                    }
                }
                this.setPostConstructors(postConstructors);
                this.setJsonFiles(files);
            } catch (FileNotFoundException ex) {
                log.warn(ex.getMessage(), ex);
            } catch (IOException ex) {
                log.warn(ex.getMessage(), ex);
            }
        }else{
            log.warn("Cannot read configuration file from {}, it does not exist?");
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException{
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception{
        initializeWithConfigurationFile();
    }

    @Override
    public String toString(){
        return super.toString();
    }
}

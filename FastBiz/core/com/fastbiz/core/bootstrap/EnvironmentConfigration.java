package com.fastbiz.core.bootstrap;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class EnvironmentConfigration{

    private static final String        APPLICATION_HOME_KEY           = "FASTBIZ_HOME";

    private static String              bootstrapConfigFilePath;

    public static String               SOLUTIONS_DIR_ATTR             = "SOLUTIONS_DIR";

    private static Map<String, Object> attributes                     = new ConcurrentHashMap<String, Object>();

    private static final String        CLASS_PATH_RESOURCE_URL_PREFIX = "classpath:";

    public static void setApplicationHome(String home){
        System.setProperty(APPLICATION_HOME_KEY, home);
    }

    public static String getBaseDir(){
        String home = System.getProperty(APPLICATION_HOME_KEY);
        if (home == null) {
            home = System.getenv(APPLICATION_HOME_KEY);
            if (home == null) {
                home = System.getProperty("user.dir");
                System.setProperty(APPLICATION_HOME_KEY, home);
            }
        }
        return home;
    }

    public static Object getAttribute(String attributeName){
        return attributes.get(attributeName);
    }

    public static void setAttribute(String attributeName, Object attribute){
        attributes.put(attributeName, attribute);
    }

    public static String getBootstrapConfigFilePath(){
        return bootstrapConfigFilePath;
    }

    public static void setBootstrapConfigFilePath(String bootstrapConfigFilePath){
        if (bootstrapConfigFilePath.startsWith(CLASS_PATH_RESOURCE_URL_PREFIX)) {
            EnvironmentConfigration.bootstrapConfigFilePath = bootstrapConfigFilePath;
        } else {
            try {
                String url = new File(getBaseDir(), bootstrapConfigFilePath).toURI().toURL().toString();
                EnvironmentConfigration.bootstrapConfigFilePath = url;
            } catch (MalformedURLException e) {
                EnvironmentConfigration.bootstrapConfigFilePath = bootstrapConfigFilePath;
            }
        }
    }
}

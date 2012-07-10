package com.fastbiz.boot.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Main{

    private static final String BOOT_CONF_FILE                       = "boot.properties";

    private static final String BOOTSTARUP_CONF_FILE                 = "conf/bootstrap.xml";

    private static final String BOOTSTRAP_CONTROLLER_CLASS_NAME      = "com.fastbiz.core.bootstrap.BootstrapController";

    private static final String ENVIRONMENT_CONFIGURATION_CLASS_NAME = "com.fastbiz.core.bootstrap.EnvironmentConfigration";

    public static void main(String args[]) throws MalformedURLException, ClassNotFoundException, InstantiationException,
                    IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException,
                    NoSuchMethodException{
        Properties startupProperties = getBootConfiguration();
        ClassLoader bootStrapClassLoader = setBootStrapClassLoader(startupProperties);
        Class<?> bootstrapControllerClass = bootStrapClassLoader.loadClass(BOOTSTRAP_CONTROLLER_CLASS_NAME);
        Class<?> environmentConfigurationClass = bootStrapClassLoader.loadClass(ENVIRONMENT_CONFIGURATION_CLASS_NAME);
        Method setConfigMethod = environmentConfigurationClass.getMethod("setBootstrapConfigFilePath", String.class);
        setConfigMethod.invoke(null, new Object[] { BOOTSTARUP_CONF_FILE });
        Object instance = bootstrapControllerClass.getConstructor().newInstance();
        Method initMethod = bootstrapControllerClass.getMethod("init");
        Method startMethod = bootstrapControllerClass.getMethod("start");
        Method joinMethod = bootstrapControllerClass.getMethod("join");
        Method shutdownMethod = bootstrapControllerClass.getMethod("shutdown");
        Method setClassLoaderMethod = bootstrapControllerClass.getMethod("setBootstrapClassLoader", ClassLoader.class);
        setClassLoaderMethod.invoke(instance, new Object[] { bootStrapClassLoader });
        initMethod.invoke(instance, new Object[0]);
        startMethod.invoke(instance, new Object[0]);
        joinMethod.invoke(instance, new Object[0]);
        shutdownMethod.invoke(instance, new Object[0]);
        System.exit(0);
    }

    private static ClassLoader setBootStrapClassLoader(Properties proerties) throws MalformedURLException{
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        if (parent == null) {
            parent = Main.class.getClassLoader();
        }
        if (parent == null) {
            parent = ClassLoader.getSystemClassLoader();
        }
        BootstrapClassLoader bc = new BootstrapClassLoader(parent);
        if (proerties != null) {
            String classpathes = (String) proerties.get("startup.classpath");
            if (classpathes != null) {
                StringTokenizer tokenizer = new StringTokenizer(classpathes, ";");
                while (tokenizer.hasMoreTokens()) {
                    String pathPattern = tokenizer.nextToken();
                    setClassPathFromPathPattern(bc, pathPattern);
                }
            }
        }
        Thread.currentThread().setContextClassLoader(bc);
        return bc;
    }

    protected static void setClassPathFromPathPattern(BootstrapClassLoader classLoader, String pathPattern)
                    throws MalformedURLException{
        String normalPathPattern = pathPattern.replace("\\", "/");
        StringTokenizer tokenizer = new StringTokenizer(normalPathPattern, "/");
        final List<String> parts = new ArrayList<String>();
        while (tokenizer.hasMoreTokens()) {
            parts.add(tokenizer.nextToken());
        }
        if (parts.size() == 0) {
            System.out.println("Invalid classpath part " + pathPattern);
        } else if (parts.size() == 1) {
            File file = new File(parts.get(0));
            if (!file.isAbsolute()) {
                file = new File(file.getPath());
            }
            classLoader.addFile(file);
        } else if (parts.size() > 1) {
            int lastSlashIndx = normalPathPattern.lastIndexOf("/");
            String parentPath = normalPathPattern.substring(0, lastSlashIndx);
            File parentFile = new File(parentPath);
            if (!parentFile.isAbsolute()) {
                parentFile = new File(parentFile.getPath());
            }
            if (parentFile.exists() && parentFile.isDirectory()) {
                if (parts.get(parts.size() - 1).equals("**")) {
                    classLoader.addDirectoryFiles(parentFile, true);
                    return;
                }
                try {
                    Pattern.compile(parts.get(parts.size() - 1));
                } catch (PatternSyntaxException ex) {
                    return;
                }
                File[] subFiles = parentFile.listFiles(new FilenameFilter(){

                    public boolean accept(File dir, String name){
                        return Pattern.matches(parts.get(parts.size() - 1), name);
                    }
                });
                if (subFiles != null) {
                    for (File subFile : subFiles) {
                        classLoader.addFile(subFile);
                    }
                }
            }
        }
    }

    private static Properties getBootConfiguration(){
        Properties p = new Properties();
        String home = ".";
        File startupConfigFile = new File(home, BOOT_CONF_FILE);
        if (startupConfigFile.exists()) {
            try {
                p.load(new FileInputStream(startupConfigFile));
            } catch (FileNotFoundException e) {
                String format = "Cannot find default boot configuration file %s, ignore.";
                System.out.println(String.format(format, startupConfigFile.getAbsolutePath()));
            } catch (IOException e) {
                String format = "Boot configuration file %s reading error, ignore.";
                System.out.println(String.format(format, startupConfigFile));
            }
        }
        return p;
    }
}

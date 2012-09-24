package com.fastbiz.boot.main;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main{

    private static final String BOOT_CONF_FILE_NAME                  = "boot_classpath.conf";

    private static final String BOOTSTARUP_CONF_FILE                 = "conf/bootstrap.xml";

    private static final String BOOTSTRAP_CONTROLLER_CLASS_NAME      = "com.fastbiz.core.bootstrap.BootstrapController";

    private static final String ENVIRONMENT_CONFIGURATION_CLASS_NAME = "com.fastbiz.core.bootstrap.EnvironmentConfigration";

    public static void main(String args[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException,
                    IllegalArgumentException, InvocationTargetException, SecurityException, NoSuchMethodException{
        ClassLoader bootStrapClassLoader = setBootStrapClassLoader();
        Class<?> bootstrapControllerClass = bootStrapClassLoader.loadClass(BOOTSTRAP_CONTROLLER_CLASS_NAME);
        Class<?> environmentConfigurationClass = bootStrapClassLoader.loadClass(ENVIRONMENT_CONFIGURATION_CLASS_NAME);
        Method setConfigMethod = environmentConfigurationClass.getMethod("setBootstrapConfigFilePath", String.class);
        setConfigMethod.invoke(null, new Object[] { BOOTSTARUP_CONF_FILE });
        Object instance = bootstrapControllerClass.getConstructor().newInstance();
        Method initMethod = bootstrapControllerClass.getMethod("init");
        Method startMethod = bootstrapControllerClass.getMethod("start");
        Method joinMethod = bootstrapControllerClass.getMethod("join");
        Method shutdownMethod = bootstrapControllerClass.getMethod("shutdown");
        initMethod.invoke(instance, new Object[0]);
        startMethod.invoke(instance, new Object[0]);
        joinMethod.invoke(instance, new Object[0]);
        shutdownMethod.invoke(instance, new Object[0]);
        System.exit(0);
    }

    private static ClassLoader setBootStrapClassLoader(){
        ClassLoader parent = Thread.currentThread().getContextClassLoader();
        if (parent == null) {
            parent = Main.class.getClassLoader();
        }
        if (parent == null) {
            parent = ClassLoader.getSystemClassLoader();
        }
        BootstrapClassLoader bc = new BootstrapClassLoader(new File(BOOT_CONF_FILE_NAME), parent);
        Thread.currentThread().setContextClassLoader(bc);
        return bc;
    }
}

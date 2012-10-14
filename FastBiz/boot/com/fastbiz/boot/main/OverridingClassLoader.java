package com.fastbiz.boot.main;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.Set;

public class OverridingClassLoader extends URLClassLoader{

    public static final String[] DEFAULT_EXCLUDED_PACKAGES = new String[] { "java.", "javax.", "sun.", "oracle." };

    private static final String  CLASS_FILE_SUFFIX         = ".class";

    private final Set<String>    excludedPackages          = new HashSet<String>();

    private final Set<String>    excludedClasses           = new HashSet<String>();

    private final Object         exclusionMonitor          = new Object();

    public OverridingClassLoader(ClassLoader parent) {
        this(new URL[0], parent);
    }

    public OverridingClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        for (String packageName : DEFAULT_EXCLUDED_PACKAGES) {
            excludePackage(packageName);
        }
    }

    public void excludePackage(String packageName){
        if (packageName == null) {
            throw new IllegalArgumentException("Package must not be null");
        }
        synchronized (this.exclusionMonitor) {
            this.excludedPackages.add(packageName);
        }
    }

    public void excludeClass(String className){
        if (className == null) {
            throw new IllegalArgumentException("className must not be null");
        }
        synchronized (this.exclusionMonitor) {
            this.excludedClasses.add(className);
        }
    }

    protected boolean isExcluded(String className){
        synchronized (this.exclusionMonitor) {
            if (this.excludedClasses.contains(className)) {
                return true;
            }
            for (String packageName : this.excludedPackages) {
                if (className.startsWith(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException{
        Class<?> result = null;
        if (isEligibleForOverriding(name)) {
            result = loadClassForOverriding(name);
        }
        if (result != null) {
            if (resolve) {
                resolveClass(result);
            }
            return result;
        } else {
            return super.loadClass(name, resolve);
        }
    }

    protected boolean isEligibleForOverriding(String className){
        return !isExcluded(className);
    }

    protected Class<?> loadClassForOverriding(String name) throws ClassNotFoundException{
        Class<?> result = findLoadedClass(name);
        if (result == null) {
            byte[] bytes = loadBytesForClass(name);
            if (bytes != null) {
                result = defineClass(name, bytes, 0, bytes.length);
                if (result.getPackage() == null) {
                    int packageSeparator = name.lastIndexOf('.');
                    if (packageSeparator != -1) {
                        String packageName = name.substring(0, packageSeparator);
                        definePackage(packageName, null, null, null, null, null, null, null);
                    }
                }
            }
        }
        return result;
    }

    protected byte[] loadBytesForClass(String name) throws ClassNotFoundException{
        InputStream is = openStreamForClass(name);
        if (is == null) {
            return null;
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            copy(is, out);
            byte[] bytes = out.toByteArray();
            return transformIfNecessary(name, bytes);
        } catch (IOException ex) {
            throw new ClassNotFoundException("Cannot load resource for class [" + name + "]", ex);
        }
    }

    private static int copy(InputStream in, OutputStream out) throws IOException{
        try {
            int byteCount = 0;
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            out.flush();
            return byteCount;
        } finally {
            try {
                in.close();
            } catch (IOException ex) {}
            try {
                out.close();
            } catch (IOException ex) {}
        }
    }

    protected InputStream openStreamForClass(String name){
        String internalName = name.replace('.', '/') + CLASS_FILE_SUFFIX;
        return getResourceAsStream(internalName);
    }

    protected byte[] transformIfNecessary(String name, byte[] bytes){
        return bytes;
    }
}

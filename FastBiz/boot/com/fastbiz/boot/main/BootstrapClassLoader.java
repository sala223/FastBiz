package com.fastbiz.boot.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class BootstrapClassLoader extends InstrumentableClassLoader{

    private static final String CLASSPATH_PROP       = "classpath";

    private static final String IGNORE_PACKAGES_PROP = "ignore-packages";

    private static final String IGNORE_CLASSES_PROP  = "ignore-classes";

    public BootstrapClassLoader(File configurationFile, ClassLoader parent) {
        super(new URL[0], parent);
        setConfiguration(readConfiguration(configurationFile));
    }

    public BootstrapClassLoader(ClassLoader parent) {
        this(null, parent);
    }

    @Override
    public URL findResource(String name){
        URL resource = super.findResource(name);
        if (resource == null) {
            URL[] urls = this.getURLs();
            for (URL url : urls) {
                String parent = url.getFile();
                File resourceFile = new File(parent, name);
                if (resourceFile.exists()) {
                    try {
                        return resourceFile.toURI().toURL();
                    } catch (MalformedURLException e) {}
                }
            }
        }
        return resource;
    }

    protected void addURL(File file) throws MalformedURLException{
        this.addURL(file.toURI().toURL());
    }

    protected void addFileByPattern(File file, String pattern) throws MalformedURLException{
        PathMatcher matcher = new PathMatcher();
        if (file == null || !file.exists()) {
            return;
        }
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (matcher.match(pattern, f.getPath().replaceAll(Pattern.quote("\\"), PathMatcher.DEFAULT_PATH_SEPARATOR))) {
                    addURL(f);
                    if (f.isDirectory()) {
                        addFileByPattern(f, pattern);
                    }
                }
            }
        }
    }

    protected void setClassPathFromPathPattern(String pathPattern) throws MalformedURLException{
        PathMatcher pathMatcher = new PathMatcher();
        boolean isPattern = pathMatcher.isPattern(pathPattern);
        if (isPattern) {
            String startPath = pathMatcher.getPatternStartPath(pathPattern);
            File file = new File(startPath);
            this.addFileByPattern(file, pathPattern);
        } else {
            this.addURL(new File(pathPattern));
        }
    }

    protected Properties readConfiguration(File configurationFile){
        Properties properties = new Properties();
        try {
            if (configurationFile != null && configurationFile.exists()) {
                properties.load(new FileInputStream(configurationFile));
            }
        } catch (FileNotFoundException e) {
            String format = "Classloader configuration file %s does not exist, ignore.";
            System.out.println(String.format(format, configurationFile.getAbsolutePath()));
        } catch (IOException ex) {
            String format = "Errors when reading classloader configuration file %s, ignore.";
            System.err.println(String.format(format, configurationFile.getAbsolutePath()));
        }
        return properties;
    }

    protected void setConfiguration(Properties properties){
        if (properties.containsKey(CLASSPATH_PROP)) {
            String classpathes = (String) properties.get(CLASSPATH_PROP);
            if (classpathes != null) {
                StringTokenizer tokenizer = new StringTokenizer(classpathes, ";");
                while (tokenizer.hasMoreTokens()) {
                    String pathPattern = tokenizer.nextToken();
                    try {
                        setClassPathFromPathPattern(pathPattern);
                    } catch (MalformedURLException ex) {
                        String format = "Incorrect path pattern %s, ignore.";
                        System.err.println(String.format(format, pathPattern));
                    }
                }
            }
        }
        if (properties.containsKey(IGNORE_CLASSES_PROP)) {
            String ignoreClasses = (String) properties.get(IGNORE_CLASSES_PROP);
            if (ignoreClasses != null) {
                StringTokenizer tokenizer = new StringTokenizer(ignoreClasses, ";");
                while (tokenizer.hasMoreTokens()) {
                    String ignoreClass = tokenizer.nextToken();
                    this.excludeClass(ignoreClass);
                }
            }
        }
        if (properties.containsKey(IGNORE_PACKAGES_PROP)) {
            String ignorePackages = (String) properties.get(IGNORE_PACKAGES_PROP);
            if (ignorePackages != null) {
                StringTokenizer tokenizer = new StringTokenizer(ignorePackages, ";");
                while (tokenizer.hasMoreTokens()) {
                    String ignorePackage = tokenizer.nextToken();
                    this.excludePackage(ignorePackage);
                }
            }
        }
    }

    public String print(){
        URL[] urls = this.getURLs();
        StringBuilder buffer = new StringBuilder();
        buffer.append("{\n");
        for (URL url : urls) {
            buffer.append(url.toString() + File.pathSeparator + "\n");
        }
        buffer.append("}");
        return buffer.toString();
    }
}

package com.fastbiz.boot.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class BootstrapClassLoader extends URLClassLoader{

    public BootstrapClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public BootstrapClassLoader(File configurationFile, ClassLoader parent) {
        this(parent);
        setClassPathFromConfigurationFile(configurationFile);
    }

    public BootstrapClassLoader(ClassLoader parent) {
        this(new URL[0], parent);
    }

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

    public void addURL(File file) throws MalformedURLException{
        if (file != null && file.exists()) {
            this.addURL(file.toURI().toURL());
        }
    }

    public void addDirectoryFiles(File directory, boolean isRecursive) throws MalformedURLException{
        if (directory == null || !directory.exists()) {
            return;
        }
        File[] files = null;
        if (!isRecursive) {
            files = directory.listFiles(new FileFilter(){

                public boolean accept(File pathname){
                    if (pathname.isFile()) {
                        return true;
                    }
                    return false;
                }
            });
        } else {
            files = directory.listFiles();
        }
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory() && isRecursive) {
                    addDirectoryFiles(file, true);
                } else {
                    this.addURL(file);
                }
            }
        }
    }

    protected void setClassPathFromPathPattern(String pathPattern) throws MalformedURLException{
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
            this.addURL(file);
        } else if (parts.size() > 1) {
            int lastSlashIndx = normalPathPattern.lastIndexOf("/");
            String parentPath = normalPathPattern.substring(0, lastSlashIndx);
            File parentFile = new File(parentPath);
            if (!parentFile.isAbsolute()) {
                parentFile = new File(parentFile.getPath());
            }
            if (parentFile.exists() && parentFile.isDirectory()) {
                if (parts.get(parts.size() - 1).equals("**")) {
                    this.addDirectoryFiles(parentFile, true);
                    return;
                } else if (parts.get(parts.size() - 1).equals("*")) {
                    File[] files = parentFile.listFiles();
                    for (File file : files) {
                        this.addURL(file);
                    }
                    return;
                }
                File[] subFiles = parentFile.listFiles(new FilenameFilter(){

                    public boolean accept(File dir, String name){
                        return Pattern.matches(parts.get(parts.size() - 1), name);
                    }
                });
                if (subFiles != null) {
                    for (File subFile : subFiles) {
                        this.addURL(subFile);
                    }
                }
            }
        }
    }

    protected void setClassPathFromConfigurationFile(File configuraitonFile){
        if (configuraitonFile != null && configuraitonFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(configuraitonFile));
                String classpathes = reader.readLine();
                while (classpathes != null) {
                    StringTokenizer tokenizer = new StringTokenizer(classpathes, ";");
                    while (tokenizer.hasMoreTokens()) {
                        String pathPattern = tokenizer.nextToken();
                        setClassPathFromPathPattern(pathPattern);
                    }
                    classpathes = reader.readLine();
                }
            } catch (IOException e) {
                String format = "Errors when reading classpath configuration file %s, ignore.";
                System.out.println(String.format(format, configuraitonFile.getAbsolutePath()));
            }
        } else {
            String format = "Classpath configuration file %s doesn't exist, ignore.";
            System.out.println(String.format(format, configuraitonFile == null ? "" : configuraitonFile.getAbsolutePath()));
        }
    }

    public String toString(){
        URL[] urls = this.getURLs();
        StringBuffer buffer = new StringBuffer();
        buffer.append("{\n");
        for (URL url : urls) {
            buffer.append(url.toString() + File.pathSeparator + "\n");
        }
        buffer.append("}");
        return buffer.toString();
    }
}

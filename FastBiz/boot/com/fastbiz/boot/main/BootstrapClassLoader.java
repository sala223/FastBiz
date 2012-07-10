package com.fastbiz.boot.main;


import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class BootstrapClassLoader extends URLClassLoader{

    public BootstrapClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
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

    public void addFile(File file) throws MalformedURLException{
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
                    this.addFile(file);
                }
            }
        }
    }

    public String toString(){
        URL[] urls = this.getURLs();
        StringBuffer buffer = new StringBuffer();
        buffer.append("URLS{\n");
        for (URL url : urls) {
            buffer.append(url.toString() + File.pathSeparator +"\n");
        }
        buffer.append(" }");
        return buffer.toString();
    }
}

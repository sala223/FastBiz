package com.fastbiz.boot.main;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

public class InstrumentableClassLoader extends OverridingClassLoader{

    private List<ClassFileTransformer> transformers = new ArrayList<ClassFileTransformer>();

    public InstrumentableClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        transformers = new ArrayList<ClassFileTransformer>();
    }

    public InstrumentableClassLoader(ClassLoader parent) {
        super(parent);
        transformers = new ArrayList<ClassFileTransformer>();
    }

    public ClassLoader getThrowawayClassLoader(){
        URL[] urls = this.getURLs();
        return new URLClassLoader(urls,this.getParent()); 
    }

    public void addTransformer(ClassFileTransformer transformer){
        transformers.add(transformer);
    }

    public byte[] transformIfNecessary(String className, byte[] bytes){
        String internalName = className.replace(".", "/");
        return transformIfNecessary(className, internalName, bytes, null);
    }

    public byte[] transformIfNecessary(String className, String internalName, byte[] bytes, ProtectionDomain pd){
        byte[] result = bytes;
        for (ClassFileTransformer cft : this.transformers) {
            try {
                byte[] transformed = cft.transform(this, internalName, null, pd, result);
                if (transformed != null) {
                    result = transformed;
                }
            } catch (IllegalClassFormatException ex) {
                throw new IllegalStateException("Class file transformation failed", ex);
            }
        }
        return result;
    }
}

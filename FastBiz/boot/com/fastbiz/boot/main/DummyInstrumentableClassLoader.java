package com.fastbiz.boot.main;

import java.lang.instrument.ClassFileTransformer;
import java.net.URL;
import java.net.URLClassLoader;

public class DummyInstrumentableClassLoader extends URLClassLoader{

    public DummyInstrumentableClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public DummyInstrumentableClassLoader(ClassLoader parent) {
        this(new URL[0], parent);
    }

    public void addTransformer(ClassFileTransformer transformer){}

    public ClassLoader getThrowawayClassLoader(){
        URL[] urls = this.getURLs();
        return new URLClassLoader(urls, this.getParent()); 
    }
}

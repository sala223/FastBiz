package com.fastbiz.common.structure;

import java.util.AbstractSet;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

public class OrderedProperties extends Properties{

    private static final long serialVersionUID = 1L;

    private Vector<Object>    keys             = new Vector<Object>();

    public OrderedProperties() {}

    public Enumeration<?> propertyNames(){
        return keys.elements();
    }

    public synchronized Object put(Object key, Object value){
        if (keys.contains(key)) {
            keys.remove(key);
        }
        keys.add(key);
        return super.put(key, value);
    }

    public synchronized Object remove(Object key){
        keys.remove(key);
        return super.remove(key);
    }

    @Override
    public synchronized void clear(){
        super.clear();
        keys.clear();
    }

    @Override
    public synchronized Enumeration<Object> keys(){
        return keys.elements();
    }

    @Override
    public Set<Object> keySet(){
        return Collections.synchronizedSet(new KeySet());
    }

    private class KeySet extends AbstractSet<Object>{

        public Iterator<Object> iterator(){
            return keys.iterator();
        }

        public int size(){
            return keys.size();
        }

        public boolean contains(Object key){
            return keys.contains(key);
        }

        public boolean remove(Object key){
            return OrderedProperties.this.remove(key) != null;
        }

        public void clear(){
            OrderedProperties.this.clear();
        }
    }
}

package com.fastbiz.core.service.provisioning;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProvisioningContext{

    private Map<String, ProvisioningStatus> status = new ConcurrentHashMap<String, ProvisioningStatus>();

    public synchronized void addBeanRunningStatus(Class<?> beanClass){
        status.put(beanClass.getName(), new ProvisioningStatus(beanClass));
    }

    public synchronized void addBeanRunningStatus(Class<?> beanClass, Throwable ex){
        status.put(beanClass.getName(), new ProvisioningStatus(beanClass, ex));
    }

    public boolean hasError(){
        return status.size() != 0;
    }

    public ProvisioningStatus getBeanRunningStatus(Class<?> beanClass){
        return status.get(beanClass.getName());
    }

    public List<ProvisioningStatus> listBeansWithError(){
        List<ProvisioningStatus> beans = new ArrayList<ProvisioningStatus>();
        for (ProvisioningStatus ps : status.values()) {
            if (!ps.isSuccess()) {
                beans.add(ps);
            }
        }
        return beans;
    }
    
    public List<ProvisioningStatus> listBeansWithoutError(){
        List<ProvisioningStatus> beans = new ArrayList<ProvisioningStatus>();
        for (ProvisioningStatus ps : status.values()) {
            if (ps.isSuccess()) {
                beans.add(ps);
            }
        }
        return beans;
    }

    public static class ProvisioningStatus{

        private String    beanClassName;

        private Throwable error;

        private boolean   isSuccess = true;

        public ProvisioningStatus(String beanClassName) {
            this.beanClassName = beanClassName;
        }

        public ProvisioningStatus(String beanClassName, Throwable error) {
            this.beanClassName = beanClassName;
            this.error = error;
            isSuccess = false;
        }

        public ProvisioningStatus(Class<?> beanClass) {
            this(beanClass.getName());
        }

        public ProvisioningStatus(Class<?> beanClass, Throwable error) {
            this(beanClass.getName(), error);
        }

        public String getBeanClassName(){
            return beanClassName;
        }

        public Throwable getError(){
            return error;
        }

        public boolean isSuccess(){
            return isSuccess;
        }
    }
}

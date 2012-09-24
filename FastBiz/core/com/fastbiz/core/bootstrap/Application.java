package com.fastbiz.core.bootstrap;

import java.util.HashMap;
import java.util.Map;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import com.fastbiz.core.bootstrap.service.BootstrapService;

public class Application{

    private static Application                                       instance;

    private ResourceLoader                                           resourceLoader = new DefaultResourceLoader();

    private Map<Class<? extends BootstrapService>, BootstrapService> services       = new HashMap<Class<? extends BootstrapService>, BootstrapService>();

    private Application() {}

    public static Application getApplication(){
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    public ResourceLoader getResourceLoader(){
        return resourceLoader;
    }

    void addBootstrapService(BootstrapService service){
        services.put(service.getClass(), service);
    }

    void removeBootstrapService(BootstrapService service){
        if (service != null) {
            services.remove(service.getClass());
        }
    }

    void removeBootstrapService(Class<? extends BootstrapService> serviceClass){
        if (serviceClass != null) {
            services.remove(serviceClass);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends BootstrapService> T getBootstrapService(Class<T> type){
        return (T) services.get(type);
    }
}

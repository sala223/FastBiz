package com.fastbiz.core.bootstrap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.util.Assert;
import com.fastbiz.common.utils.xml.XmlUnmarshaller;
import com.fastbiz.core.bootstrap.service.BootstrapService;
import com.fastbiz.core.bootstrap.service.ServiceEventListener;

@XmlRootElement(name = "services")
public class BootstrapConfiguration{

    @XmlElement(name = "service")
    private List<ServiceInformation> services = new ArrayList<ServiceInformation>();

    public BootstrapConfiguration() {}

    public BootstrapConfiguration(String bootstrapConfigFilePath) {
        XmlUnmarshaller<BootstrapConfiguration> um = new XmlUnmarshaller<BootstrapConfiguration>(BootstrapConfiguration.class);
        BootstrapConfiguration obj = um.unmarshall(new DefaultResourceLoader().getResource(bootstrapConfigFilePath));
        services = obj.services;
    }

    public List<ServiceInformation> getBootstrapServices(){
        if (services == null) {
            return new ArrayList<ServiceInformation>();
        } else {
            return Collections.unmodifiableList(services);
        }
    }

    public void addBootstrapService(ServiceInformation service){
        Assert.notNull(service);
        for (ServiceInformation serviceInf : services) {
            if (serviceInf.getServiceClass() == service.getClass()) {
                List<Class<? extends ServiceEventListener>> newlistenerClasses = service.getServiceEventListenerClasses();
                for (Class<? extends ServiceEventListener> newListener : newlistenerClasses) {
                    serviceInf.addServiceEventListener(newListener);
                }
                return;
            }
        }
        services.add(service);
    }

    public static class ServiceInformation{

        @XmlAttribute(name = "class")
        private Class<? extends BootstrapService>           serviceClass;

        @XmlElement(name = "listener")
        private List<Class<? extends ServiceEventListener>> listeners = new ArrayList<Class<? extends ServiceEventListener>>();

        ServiceInformation() {}

        public ServiceInformation(Class<? extends BootstrapService> service) {
            this.serviceClass = service;
        }

        public Class<? extends BootstrapService> getServiceClass(){
            return serviceClass;
        }

        public List<Class<? extends ServiceEventListener>> getServiceEventListenerClasses(){
            if (listeners == null) {
                return new ArrayList<Class<? extends ServiceEventListener>>();
            } else {
                return Collections.unmodifiableList(listeners);
            }
        }

        public void addServiceEventListener(Class<? extends ServiceEventListener> newListener){
            Assert.notNull(newListener);
            if (!listeners.contains(newListener)) {
                listeners.add(newListener);
            }
        }

        public String toString(){
            StringBuffer buffer = new StringBuffer();
            buffer.append("service class=" + serviceClass);
            buffer.append("service listeners={");
            for (Class<? extends ServiceEventListener> listenerClass : listeners) {
                buffer.append(listenerClass.getName() + ";");
            }
            buffer.append("}");
            return buffer.toString();
        }
    }

    public static void main(String args[]) throws Throwable{
        BootstrapConfiguration bc = new BootstrapConfiguration("bootstrap.xml");
        JAXBContext context = JAXBContext.newInstance(BootstrapConfiguration.class);
        context.createMarshaller().marshal(bc, System.out);
    }
}

package com.fastbiz.core.bootstrap.service.appserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.xml.sax.InputSource;
import com.fastbiz.common.utils.ClassUtils;
import com.fastbiz.core.bootstrap.Application;
import com.fastbiz.core.bootstrap.service.BootstrapServiceBase;
import com.fastbiz.core.bootstrap.service.BootstrapServiceException;
import com.fastbiz.core.bootstrap.service.appserver.EmbeddedConfiguration.Scan;

public class ApplicationServer extends BootstrapServiceBase{

    public static final String          COFIG_FILE             = "classpath:appserver.xml";

    private Embedded                    embedded;

    private List<ApplicationDescriptor> applicationDescriptors = new ArrayList<ApplicationDescriptor>();

    private EmbeddedConfigurationReader configurationReader;

    private static final Logger         log                    = LoggerFactory.getLogger(ApplicationServer.class);

    private EmbeddedConfiguration       configuration;

    @SuppressWarnings("unchecked")
    @Override
    public void init(Application application){
        Resource configResource = application.getResourceLoader().getResource(COFIG_FILE);
        try {
            configuration = readEmbeddedConfiguration(configResource);
        } catch (Throwable ex) {
            String fmt = "Failed loading application server configuration from %s";
            throw new BootstrapServiceException(this.getClass().getName(), ex, fmt, COFIG_FILE);
        }
        String startupClassName = configuration.getSpec().getStartupClassName();
        Class<?> startupClass;
        try {
            startupClass = ClassUtils.forName(startupClassName, Thread.currentThread().getContextClassLoader());
        } catch (Throwable ex) {
            String fmt = "Unable to load starup class %s";
            throw new BootstrapServiceException(this.getClass().getName(), fmt, startupClassName);
        }
        if (!ClassUtils.isAssignable(Embedded.class, startupClass)) {
            String fmt = "starup class %s is not inherit from %s";
            throw new BootstrapServiceException(this.getClass().getName(), fmt, startupClassName, Embedded.class.getName());
        }
        Class<? extends Embedded> provider = (Class<? extends Embedded>) startupClass;
        log.info("Create application server provider: {}", provider.getClass().getName());
        try {
            embedded = provider.newInstance();
        } catch (Exception ex) {
            String fmt = "Cannot create server implementation instance from class %s";
            throw new BootstrapServiceException(this.getClass().getName(), ex, fmt, provider.getClass());
        }
        embedded.init(configuration.getSpec());
    }

    public void setConfigurationReader(EmbeddedConfigurationReader configurationReader){
        this.configurationReader = configurationReader;
    }

    @Override
    public void start(Application application){
        applicationDescriptors = scanApplications(configuration);
        for (ApplicationDescriptor app : applicationDescriptors) {
            embedded.addApplicationContext(app);
        }
        embedded.start();
    }

    @Override
    public void stop(Application application){
        if (embedded != null) {
            embedded.stop();
        }
    }

    protected List<ApplicationDescriptor> scanApplications(EmbeddedConfiguration ec){
        ApplicationRepository repository = new ApplicationRepository();
        List<Scan> scans = ec.getScans();
        if (scans != null) {
            for (Scan scan : scans) {
                if (scan.isApplicationSet()) {
                    repository.addApplicationSet(new File(scan.getLocation()));
                } else {
                    repository.addApplication(new File(scan.getLocation()));
                }
            }
        }
        repository.scan();
        return repository.getApplications();
    }

    public List<ApplicationDescriptor> getApplicationDescriptors(){
        return applicationDescriptors;
    }

    protected EmbeddedConfiguration readEmbeddedConfiguration(Resource resource) throws IOException{
        if (this.configurationReader == null) {
            this.configurationReader = new DefaultEmbeddedConfigurationReader();
        }
        return configurationReader.readEmbeddedConfiguration(new InputSource(resource.getInputStream()));
    }
}

package com.fastbiz.core.bootstrap.service.jndi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.naming.NamingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import com.fastbiz.core.bootstrap.Application;
import com.fastbiz.core.bootstrap.service.BootstrapServiceBase;
import com.fastbiz.core.bootstrap.service.BootstrapServiceException;

public class JNDI extends BootstrapServiceBase{

    private static final Logger LOG                           = LoggerFactory.getLogger(JNDI.class);

    private static final String JNDI_CONFIG_FILE_NAME         = "jndi.properties";

    private static final String DEFAULT_CONTEXT_URLS_PROPERTY = "com.fastbiz.naming.default.urls";

    private static final String CONTEXT_PATH_SEPERATOR        = "/";

    private static final String CONTEXT_URL_SEPERATOR         = ";";

    private Properties          config                        = new Properties();

    public void init(Application application){
        try {
            Resource configResource = application.getResourceLoader().getResource("classpath:" + JNDI_CONFIG_FILE_NAME);
            config.load(configResource.getInputStream());
            if (config.containsKey(Context.INITIAL_CONTEXT_FACTORY)) {
                System.setProperty(Context.INITIAL_CONTEXT_FACTORY, config.getProperty(Context.INITIAL_CONTEXT_FACTORY));
            }
            if (config.containsKey(Context.URL_PKG_PREFIXES)) {
                System.setProperty(Context.URL_PKG_PREFIXES, config.getProperty(Context.URL_PKG_PREFIXES));
            }
            if (config.containsKey(Context.PROVIDER_URL)) {
                System.setProperty(Context.PROVIDER_URL, config.getProperty(Context.PROVIDER_URL));
            }
            if (config.containsKey(Context.SECURITY_AUTHENTICATION)) {
                System.setProperty(Context.SECURITY_AUTHENTICATION, config.getProperty(Context.SECURITY_AUTHENTICATION));
            }
            if (config.containsKey(Context.SECURITY_PRINCIPAL)) {
                System.setProperty(Context.SECURITY_PRINCIPAL, config.getProperty(Context.SECURITY_PRINCIPAL));
            }
            if (config.containsKey(Context.SECURITY_CREDENTIALS)) {
                System.setProperty(Context.SECURITY_CREDENTIALS, config.getProperty(Context.SECURITY_CREDENTIALS));
            }
        } catch (FileNotFoundException ex) {
            String fmt = "Cannot find JNDI configuration file %s";
            throw new BootstrapServiceException(this.getClass().getName(), ex, fmt, JNDI_CONFIG_FILE_NAME);
        } catch (IOException ex) {
            String fmt = "Unable to read JNDI configuration file %s";
            throw new BootstrapServiceException(this.getClass().getName(), ex, fmt, JNDI_CONFIG_FILE_NAME);
        }
        try {
            preCreateSubContext();
        } catch (NamingException ex) {
            String fmt = "Failed creating default JNDI context defined in system property %s";
            throw new BootstrapServiceException(this.getClass().getName(), ex, fmt, DEFAULT_CONTEXT_URLS_PROPERTY);
        }
    }

    protected void preCreateSubContext() throws NamingException{
        String defaultUrls = config.getProperty(DEFAULT_CONTEXT_URLS_PROPERTY);
        String[] tokens = StringUtils.tokenizeToStringArray(defaultUrls, CONTEXT_URL_SEPERATOR);
        if (tokens != null) {
            InitialContext initContext = new InitialContext(config);
            for (String token : tokens) {
                LOG.info("Create context {}", token);
                createContextRecursively(initContext, token);
            }
        }
    }

    public void bind(String contextName, Object obj) throws NamingException{
        LOG.debug("Bind object {} to {}", obj, contextName);
        InitialContext initContext = new InitialContext(config);
        int lastSeperator = contextName.lastIndexOf(CONTEXT_PATH_SEPERATOR);
        if (lastSeperator == -1) {
            initContext.bind(contextName, obj);
        } else {
            createContextRecursively(initContext, contextName.substring(0, lastSeperator));
            initContext.bind(contextName, obj);
        }
    }

    public Context createContextRecursively(Context root, String contextName) throws NamingException{
        Context context = root;
        String[] tokens = StringUtils.tokenizeToStringArray(contextName, CONTEXT_PATH_SEPERATOR);
        if (tokens == null) {
            return context;
        }
        for (String token : tokens) {
            Object boundObject = null;
            try {
                boundObject = context.lookup(token);
            } catch (NameNotFoundException ex) {}
            if (boundObject == null) {
                context = context.createSubcontext(token);
            } else if (!(boundObject instanceof Context)) {
                throw new NamingException(String.format("Path %s overwrites and already bound object", contextName));
            } else {
                context = (Context) boundObject;
            }
        }
        return context;
    }

    public void start(Application application){}

    public void stop(Application application){
        try {
            new InitialContext().close();
        } catch (NamingException ex) {
            throw new BootstrapServiceException(this.getClass().getName(), ex);
        }
    }
}

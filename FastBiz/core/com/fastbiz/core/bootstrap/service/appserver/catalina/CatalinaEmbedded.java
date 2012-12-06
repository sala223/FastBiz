package com.fastbiz.core.bootstrap.service.appserver.catalina;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.catalina.Container;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.loader.WebappLoader;
import org.apache.catalina.startup.ConnectorCreateRule;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.ContextRuleSet;
import org.apache.catalina.startup.EngineRuleSet;
import org.apache.catalina.startup.HostRuleSet;
import org.apache.catalina.startup.NamingRuleSet;
import org.apache.tomcat.util.IntrospectionUtils;
import org.apache.tomcat.util.digester.Digester;
import org.apache.tomcat.util.digester.Rule;
import org.apache.tomcat.util.digester.RuleSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import com.fastbiz.common.security.CertificateInfo;
import com.fastbiz.common.security.DataMarshaller;
import com.fastbiz.common.security.EncryptionDataMarshaller;
import com.fastbiz.common.security.KeyInfo;
import com.fastbiz.common.security.KeyStoreInfo;
import com.fastbiz.common.utils.Assert;
import com.fastbiz.common.utils.FileUtils;
import com.fastbiz.common.utils.SecurityUtils;
import com.fastbiz.common.utils.StringUtils;
import com.fastbiz.core.bootstrap.service.BootstrapServiceException;
import com.fastbiz.core.bootstrap.service.appserver.ApplicationDescriptor;
import com.fastbiz.core.bootstrap.service.appserver.ApplicationDescriptor.ApplicationType;
import com.fastbiz.core.bootstrap.service.appserver.Embedded;
import com.fastbiz.core.bootstrap.service.appserver.EmbeddedSpec;

public class CatalinaEmbedded implements Embedded{

    private static final Logger log                        = LoggerFactory.getLogger(CatalinaEmbedded.class);

    private DataMarshaller      sdp                        = new EncryptionDataMarshaller();

    protected CatalinaSpec      spec;

    protected Charset           charset;

    private StandardServer      server;

    private static String[]     beanConfigurationLocations = { "WEB-INF/web-beans.xml", "classpath:META-INF/cxf/cxf.xml",
                    "classpath:META-INF/cxf/cxf-servlet.xml" };

    public void setServer(Server server){
        if (server instanceof StandardServer) {
            this.server = (StandardServer) server;
        } else {
            throw new IllegalArgumentException("server must be type of " + StandardServer.class.getName());
        }
    }

    public Server getServer(){
        if (this.server == null) {
            try {
                charset = Charset.forName("utf-8");
            } catch (UnsupportedCharsetException e) {
                charset = Charset.defaultCharset();
                log.warn("unsupport utf-8 charset, use default {}", charset.displayName());
            }
            initialKeyStore();
            Digester digester = createDigester();
            digester.push(this);
            try {
                digester.parse(new File(this.spec.getCatalinaConfig()));
            } catch (Exception ex) {
                String message = new String("Failed parse catalina configuration file %s");
                throw new BootstrapServiceException(this.getClass().getName(), ex, message, this.spec.getCatalinaConfig());
            }
        }
        try {
            this.server.setGlobalNamingContext(new InitialContext());
        } catch (NamingException ex) {
            throw new BootstrapServiceException(this.getClass().getName(), ex);
        }
        return this.server;
    }

    public void init(EmbeddedSpec spec){
        this.spec = (CatalinaSpec) spec;
        Assert.notNull(this.spec.getCatalinaHome(), "catalina.home must not be set to null");
        String catalinaHome = new File(this.spec.getCatalinaHome()).getAbsolutePath();
        System.setProperty("catalina.home", catalinaHome);
        System.setProperty("catalina.base", catalinaHome);
        System.setProperty("catalina.useNaming", "false");
        Class<? extends DataMarshaller> dpt = this.spec.getDataMarshallerType();
        if (dpt != null) {
            try {
                sdp = dpt.newInstance();
                log.info("Set catalina data marshaller type to {}", dpt.getName());
            } catch (Throwable e) {
                log.warn("Failed instantiate data marshaller instance of type {} because of {}", dpt.getName(), e.getMessage());
                log.info("Use default data marshaller type {}", sdp.getClass().getName());
            }
        }
        getServer();
    }

    protected Host getHost(){
        Service service = server.findServices()[0];
        Engine engine = (Engine) service.getContainer();
        Container[] children = engine.findChildren();
        for (Container child : children) {
            if (child instanceof StandardHost) {
                return (StandardHost) child;
            }
        }
        return null;
    }

    private Digester createDigester(){
        Digester digester = new Digester();
        digester.setLogger(new SLf4JLogger(Digester.class));
        digester.setValidating(false);
        digester.setRulesValidation(true);
        HashMap<Class<?>, List<String>> fakeAttributes = new HashMap<Class<?>, List<String>>();
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("className");
        fakeAttributes.put(Object.class, attrs);
        digester.setFakeAttributes(fakeAttributes);
        digester.setClassLoader(StandardServer.class.getClassLoader());
        digester.addObjectCreate("Server", "org.apache.catalina.core.StandardServer", "className");
        digester.addSetProperties("Server");
        digester.addSetNext("Server", "setServer", "org.apache.catalina.Server");
        digester.addObjectCreate("Server/GlobalNamingResources", "org.apache.catalina.deploy.NamingResources");
        digester.addSetProperties("Server/GlobalNamingResources");
        digester.addSetNext("Server/GlobalNamingResources", "setGlobalNamingResources",
                        "org.apache.catalina.deploy.NamingResources");
        digester.addObjectCreate("Server/Listener", null, "className");
        digester.addSetProperties("Server/Listener");
        digester.addSetNext("Server/Listener", "addLifecycleListener", "org.apache.catalina.LifecycleListener");
        digester.addObjectCreate("Server/Service", "org.apache.catalina.core.StandardService", "className");
        digester.addSetProperties("Server/Service");
        digester.addSetNext("Server/Service", "addService", "org.apache.catalina.Service");
        digester.addObjectCreate("Server/Service/Listener", null, "className");
        digester.addSetProperties("Server/Service/Listener");
        digester.addSetNext("Server/Service/Listener", "addLifecycleListener", "org.apache.catalina.LifecycleListener");
        digester.addObjectCreate("Server/Service/Executor", "org.apache.catalina.core.StandardThreadExecutor", "className");
        digester.addSetProperties("Server/Service/Executor");
        digester.addSetNext("Server/Service/Executor", "addExecutor", "org.apache.catalina.Executor");
        digester.addRule("Server/Service/Connector", new ConnectorCreateRule());
        digester.addRule("Server/Service/Connector", new SetConnectionPropertiesRule(this));
        digester.addSetNext("Server/Service/Connector", "addConnector", "org.apache.catalina.connector.Connector");
        digester.addObjectCreate("Server/Service/Connector/Listener", null, "className");
        digester.addSetProperties("Server/Service/Connector/Listener");
        digester.addSetNext("Server/Service/Connector/Listener", "addLifecycleListener",
                        "org.apache.catalina.LifecycleListener");
        // Add RuleSets for nested elements
        digester.addRuleSet(new NamingRuleSet("Server/GlobalNamingResources/"));
        digester.addRuleSet(new EngineRuleSet("Server/Service/"));
        digester.addRuleSet(new HostRuleSet("Server/Service/Engine/"));
        digester.addRuleSet(new ContextRuleSet("Server/Service/Engine/Host/"));
        addClusterRuleSet(digester, "Server/Service/Engine/Host/Cluster/");
        // When the 'engine' is found, set the parentClassLoader.
        SetParentClassLoaderRule ecr = new SetParentClassLoaderRule(Thread.currentThread().getContextClassLoader());
        digester.addRule("Server/Service/Engine", ecr);
        addClusterRuleSet(digester, "Server/Service/Engine/Cluster/");
        return digester;
    }

    private void addClusterRuleSet(Digester digester, String prefix){
        Class<?> clazz = null;
        Constructor<?> constructor = null;
        try {
            clazz = Class.forName("org.apache.catalina.ha.ClusterRuleSet");
            constructor = clazz.getConstructor(String.class);
            RuleSet ruleSet = (RuleSet) constructor.newInstance(prefix);
            digester.addRuleSet(ruleSet);
        } catch (Exception e) {
            log.info("Catalina Custer is not enabled as {} class cannot be found", e.getMessage());
        }
    }

    private void setStandardConfiguration(ApplicationDescriptor application, StandardContext context){
        String docBase = application.getDocBase();
        if (new File(FileUtils.newFilePath(docBase, "WEB-INF", "web-beans.xml")).exists()) {
            context.getServletContext().setInitParameter("solution-id", application.getName());
            String contextConfigLocation = StringUtils.arrayToCommaDelimitedString(beanConfigurationLocations);
            context.getServletContext().setInitParameter("contextConfigLocation", contextConfigLocation);
            context.addApplicationListener("com.fastbiz.core.web.context.ContextLoaderListener");
        }
    }

    public void addApplicationContext(ApplicationDescriptor application){
        Host host = getHost();
        StandardContext context = new StandardContext();
        ContextConfig config = new ContextConfig();
        context.addLifecycleListener(config);
        context.setName("/" + application.getName());
        context.setPath(application.getPath());
        context.setDocBase(application.getDocBase());
        context.setReloadable(true);
        context.setCookies(true);
        context.setAddWebinfClassesResources(true);
        context.setParent(host);
        setStandardConfiguration(application, context);
        host.addChild(context);
        WebappLoader loader = new WebappLoader(Thread.currentThread().getContextClassLoader());
        context.setLoader(loader);
        if (application.getType() == ApplicationType.STANDARD_FOLDER) {
            File f = new File(application.getDocBase() + File.separator + "META-INF" + File.separator + "context.xml");
            if (f.isFile()) {
                try {
                    context.setConfigFile(f.toURI().toURL());
                } catch (MalformedURLException ex) {
                    String fmt = "Invalid context.xml file %s";
                    throw new BootstrapServiceException(this.getClass().getName(), ex, fmt, f.getAbsolutePath());
                }
            }
        }
    }

    private KeyStoreInfo getKeyStoreInfo(){
        return spec.getKeyStore();
    }

    @Override
    public void start(){
        try {
            log.info("Start running catalina server");
            server.start();
        } catch (LifecycleException ex) {
            throw new BootstrapServiceException(this.getClass().getName(), ex);
        }
    }

    @Override
    public void stop(){
        try {
            server.stop();
        } catch (LifecycleException ex) {
            throw new BootstrapServiceException(this.getClass().getName(), ex);
        }
    }

    private void initialKeyStore(){
        Assert.notNull(spec.getKey());
        Assert.notNull(spec.getKeyStore());
        Assert.notNull(spec.getCertificate());
        Assert.notNull(spec.getKeyStore().getFilePath());
        boolean createKeyStore = Boolean.valueOf(spec.isKeyStoreCreate());
        boolean exportCertificate = Boolean.valueOf(spec.isExportCertificate());
        KeyStoreInfo keyStoreInfo = getKeyStoreInfo();
        KeyInfo keyInfo = spec.getKey();
        CertificateInfo certificateInfo = spec.getCertificate();
        String keyStoreFilePath = keyStoreInfo.getFilePath();
        File keyStoreFile = new File(keyStoreFilePath);
        if (!keyStoreFile.isFile() || createKeyStore) {
            if (keyStoreFile.exists()) {
                boolean isDeleted = keyStoreFile.delete();
                if (!isDeleted) {
                    String fmt = "Cannot delete existing keystore file %s";
                    throw new BootstrapServiceException(this.getClass().getName(), fmt, keyStoreFile.getAbsolutePath());
                }
            }
            log.info("Create or update key store file {}", keyStoreFilePath);
            char[] password = SecurityUtils.getRandomPassword(15);
            keyStoreInfo.setPassword(password);
            keyInfo.setPassword(password);
            SecurityUtils.genKeyPair(keyStoreInfo, keyInfo, certificateInfo);
            String passwordFileName = FileUtils.getFilename(keyStoreFile.getAbsolutePath()) + ".pin";
            writePassword(keyStoreInfo.getPassword(), new File(keyStoreFile.getParent(), passwordFileName));
        }
        if (exportCertificate) {
            File certFile = new File(keyStoreFile.getParentFile().getAbsolutePath(), keyInfo.getAlias() + ".cer");
            log.info("export certificate file to {}", certFile);
            SecurityUtils.exportCertificate(keyStoreInfo, keyInfo.getAlias(), certFile.getAbsolutePath());
        }
    }

    protected byte[] securePassword(char[] password){
        return sdp.seal(new String(password).getBytes(charset));
    }

    protected byte[] disclosePassword(byte[] encodedPassword){
        return sdp.disclose(encodedPassword);
    }

    protected void writePassword(char[] password, File passwordFile){
        Assert.notNull(password);
        Assert.notNull(passwordFile);
        String fmt = "Failed create password to file %s";
        try {
            FileOutputStream out = new FileOutputStream(passwordFile);
            String encodedPassword = StringUtils.byte2HexString(securePassword(password));
            out.write(encodedPassword.getBytes(charset));
        } catch (FileNotFoundException ex) {
            throw new BootstrapServiceException(this.getClass().getName(), ex, fmt, passwordFile.getAbsolutePath());
        } catch (IOException ex) {
            throw new BootstrapServiceException(this.getClass().getName(), ex, fmt, passwordFile.getAbsolutePath());
        }
    }

    protected char[] readPassword(File passwordFile){
        Assert.notNull(passwordFile);
        String fmt = "Failed read password from file %s";
        DataInputStream in = null;
        byte[] raw = new byte[1024];
        try {
            in = new DataInputStream(new FileInputStream(passwordFile));
            int length = in.read(raw);
            if (length == 0) {
                return new char[0];
            }
            byte[] password = StringUtils.hexString2Byte(new String(raw, 0, length, charset));
            return new String(disclosePassword(password), charset).toCharArray();
        } catch (FileNotFoundException ex) {
            throw new BootstrapServiceException(this.getClass().getName(), ex, fmt, passwordFile.getAbsolutePath());
        } catch (IOException ex) {
            throw new BootstrapServiceException(this.getClass().getName(), ex, fmt, passwordFile.getAbsolutePath());
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.warn("Failed close password file {}", passwordFile);
                }
            }
        }
    }

    final static class SetParentClassLoaderRule extends Rule{

        public SetParentClassLoaderRule(ClassLoader parentClassLoader) {
            this.parentClassLoader = parentClassLoader;
        }

        ClassLoader parentClassLoader = null;

        @Override
        public void begin(String namespace, String name, Attributes attributes) throws Exception{
            Container top = (Container) digester.peek();
            top.setParentClassLoader(parentClassLoader);
        }
    }

    final static class SetConnectionPropertiesRule extends Rule{

        private static List<String> excludes                 = new ArrayList<String>();

        private static final String KEYSTORE_PASSWORD_ATTR   = "keystorePass";

        private static final String TRUSTSTORE_PASSWORD_ATTR = "truststorePass";

        private static final String SCHEME_ATTR              = "scheme";

        private static final String SCHEME_HTTPS             = "https";

        private static final String PORT_ATTR                = "port";
        static {
            excludes.add("executor");
        }

        private CatalinaEmbedded    catalina;

        SetConnectionPropertiesRule(CatalinaEmbedded catalina) {
            this.catalina = catalina;
        }

        @Override
        public void begin(String namespace, String nameX, Attributes attributes) throws Exception{
            Boolean isSecuredConnector = null;
            for (int i = 0; i < attributes.getLength(); i++) {
                String name = attributes.getLocalName(i);
                if ("".equals(name)) {
                    name = attributes.getQName(i);
                }
                if (name.endsWith(SCHEME_ATTR)) {
                    String value = attributes.getValue(i);
                    if (SCHEME_HTTPS.equalsIgnoreCase(value)) {
                        isSecuredConnector = true;
                    } else {
                        isSecuredConnector = false;
                    }
                }
                String value = attributes.getValue(i);
                if (!excludes.contains(name)) {
                    if (KEYSTORE_PASSWORD_ATTR.equals(name)) {
                        IntrospectionUtils.setProperty(digester.peek(), KEYSTORE_PASSWORD_ATTR, getPassword(value));
                        continue;
                    } else if (TRUSTSTORE_PASSWORD_ATTR.equals(name)) {
                        IntrospectionUtils.setProperty(digester.peek(), TRUSTSTORE_PASSWORD_ATTR, getPassword(value));
                        continue;
                    }
                    if (!digester.isFakeAttribute(digester.peek(), name)
                                    && !IntrospectionUtils.setProperty(digester.peek(), name, value)
                                    && digester.getRulesValidation()) {
                        String message = "connector doesn't have a matching set property '%s'";
                        digester.getLogger().warn(String.format(message, name));
                    }
                }
            }
            if (isSecuredConnector != null) {
                if (isSecuredConnector) {
                    digester.getLogger().info(String.format("Override HTTPS port to %s", catalina.spec.getHttpsPort()));
                    IntrospectionUtils.setProperty(digester.peek(), PORT_ATTR, Integer.toString(catalina.spec.getHttpsPort()));
                } else {
                    digester.getLogger().info(String.format("Override HTTP port to %s", catalina.spec.getHttpPort()));
                    IntrospectionUtils.setProperty(digester.peek(), PORT_ATTR, Integer.toString(catalina.spec.getHttpPort()));
                }
            }
        }

        private String getPassword(String value){
            File file = getFile(value);
            char[] password = catalina.readPassword(file);
            return new String(password);
        }

        private File getFile(String value){
            return new File(System.getProperty("catalina.home"), value);
        }
    }
}

package com.fastbiz.core.bootstrap.service.datasource;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.CommonDataSource;
import javax.xml.bind.JAXBContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import com.fastbiz.common.utils.xml.XmlUnmarshaller;
import com.fastbiz.core.bootstrap.Application;
import com.fastbiz.core.bootstrap.service.BootstrapServiceBase;
import com.fastbiz.core.bootstrap.service.BootstrapServiceException;
import com.fastbiz.core.bootstrap.service.datasource.cfg.DataSourceDefinition;
import com.fastbiz.core.bootstrap.service.datasource.cfg.DataSourcesDefinition;
import com.fastbiz.core.bootstrap.service.datasource.cfg.NonTxDataSourceDefinition;
import com.fastbiz.core.bootstrap.service.datasource.cfg.XADataSourceDefinition;
import com.fastbiz.core.tenant.TenantHolder;

public class DataSourceService extends BootstrapServiceBase implements DataSourcesDefinitionSource{

    private static final String    DATA_SOURCE_FILE = "classpath:ds.xml";

    private static final Logger    LOG              = LoggerFactory.getLogger(DataSourceService.class);

    private DataSourceDefinition[] definitions;

    @Override
    public void init(Application application){
        definitions = getDataSourcesDefinition().getAllDataSourceDefinition();
    }

    @Override
    public void start(Application application){
        for (DataSourceDefinition dsd : definitions) {
            dsd.validate();
            DataSourceFactory factory = null;
            if (dsd instanceof NonTxDataSourceDefinition) {
                factory = new C3P0DataSourceFactory();
            } else if (dsd instanceof XADataSourceDefinition) {
                factory = new XADataSourceFactory();
            } else {
                LOG.warn("Unsupported data source type {}, ignore", dsd.getClass());
                continue;
            }
            if (factory != null) {
                Object ds = factory.createDataSource(dsd);
                String jndiName = dsd.getJndiName();
                DataSourceDelegator delegator = new DataSourceDelegator(new TenantHolder(), (CommonDataSource) ds);
                try {
                    LOG.info("Bind datasource {} to jndi {}", ds, jndiName);
                    InitialContext initContext = new InitialContext();
                    initContext.bind(jndiName, delegator);
                } catch (NamingException ex) {
                    String fmt = "Cannot bind datasource to jndi %s";
                    throw new BootstrapServiceException(this.getClass().getName(), ex, fmt, jndiName);
                }
            }
        }
    }

    @Override
    public void stop(Application application){}

    @Override
    public DataSourcesDefinition getDataSourcesDefinition(){
        XmlUnmarshaller<DataSourcesDefinition> unmarshaller = new XmlUnmarshaller<DataSourcesDefinition>(
                        DataSourcesDefinition.class);
        Resource resource = Application.getApplication().getResourceLoader().getResource(DATA_SOURCE_FILE);
        return unmarshaller.unmarshall(resource);
    }

    public static void main(String args[]) throws Throwable{
        DataSourcesDefinition dsd = new DataSourcesDefinition();
        dsd.addDataSourceDefinition(new XADataSourceDefinition());
        JAXBContext context = JAXBContext.newInstance(DataSourcesDefinition.class);
        context.createMarshaller().marshal(dsd, System.out);
    }
}

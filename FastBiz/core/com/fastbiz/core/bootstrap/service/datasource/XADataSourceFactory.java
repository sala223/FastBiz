package com.fastbiz.core.bootstrap.service.datasource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.apache.tomcat.jdbc.pool.XADataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fastbiz.common.utils.StringUtils;
import com.fastbiz.core.bootstrap.service.datasource.cfg.DataSourceConfigurationException;
import com.fastbiz.core.bootstrap.service.datasource.cfg.DataSourceDefinition;
import com.fastbiz.core.bootstrap.service.datasource.cfg.XADataSourceDefinition;

public class XADataSourceFactory implements DataSourceFactory{

    private static final Logger LOG = LoggerFactory.getLogger(XADataSourceFactory.class);

    @Override
    public Object createDataSource(DataSourceDefinition definition){
        if (definition instanceof XADataSourceDefinition) {
            LOG.debug("Start creating XADataSource pool with definition {}", definition);
            XADataSourceDefinition xadse = (XADataSourceDefinition) definition;
            javax.sql.XADataSource xds = createRawDataSourceInstance(xadse);
            PoolProperties pp = new PoolProperties();
            pp.setDataSource(xds);
            Properties dbProperties = new Properties();
            XADataSource xaDataSource = new XADataSource();
            xaDataSource.setPoolProperties(pp);
            xaDataSource.setDbProperties(dbProperties);
            xaDataSource.setUrl(xadse.getConnectionUrl());
            xaDataSource.setUsername(xadse.getUserName());
            xaDataSource.setPassword(xadse.getPassword());
            return xaDataSource;
        } else {
            throw new IllegalArgumentException("Can only accept argument type of " + XADataSourceDefinition.class);
        }
    }

    protected javax.sql.XADataSource createRawDataSourceInstance(XADataSourceDefinition xadse){
        String dataSourceClassName = xadse.getXaDatasourceClassName();
        Class<?> dataSourceClass;
        try {
            dataSourceClass = Thread.currentThread().getContextClassLoader().loadClass(dataSourceClassName);
        } catch (ClassNotFoundException ex) {
            throw new DataSourceConfigurationException(ex);
        }
        if (javax.sql.XADataSource.class.isAssignableFrom(dataSourceClass)) {
            try {
                Constructor<?> constructor = null;
                Object instance = null;
                try {
                    constructor = dataSourceClass.getConstructor(Properties.class);
                    instance = constructor.newInstance(xadse.getProperties());
                } catch (NoSuchMethodException ex) {
                    LOG.debug("Cannot find constructor with Porperty type as a argument. try default constructor");
                    constructor = dataSourceClass.getConstructor();
                    instance = constructor.newInstance();
                    Properties properties = xadse.getProperties();
                    Set<Entry<Object, Object>> entries = properties.entrySet();
                    for (Entry<Object, Object> entry : entries) {
                        String property = entry.getKey().toString();
                        String value = entry.getValue().toString();
                        setDataSourceProperty(instance, property, value);
                    }
                }
                return (javax.sql.XADataSource) instance;
            } catch (Throwable e) {
                throw new DataSourceConfigurationException(e, "Failed create XADataSource instance from class %s",
                                dataSourceClass);
            }
        } else {
            throw new DataSourceConfigurationException("%s is not a XADataSource sub class", dataSourceClass);
        }
    }

    protected void setDataSourceProperty(Object o, String name, String value){
        LOG.debug("Set XADataSource Property name={},value={}", name, value);
        String setter = "set" + StringUtils.capitalize(value, 0);
        try {
            Method methods[] = o.getClass().getMethods();
            for (Method method : methods) {
                Class<?> paramT[] = method.getParameterTypes();
                Object[] params = new Object[1];
                if (setter.equals(method.getName()) && paramT.length == 1) {
                    if (String.class.equals(paramT[0])) {
                        params[0] = value;
                    } else if (Integer.class.equals(paramT[0])) {
                        params[0] = new Integer(value);
                    } else if (Long.class.equals(paramT[0])) {
                        params[0] = new Long(value);
                    } else if (Boolean.class.equals(paramT[0])) {
                        params[0] = new Boolean(value);
                    } else {
                        LOG.warn("Cannot find a match method for set property {}", name);
                    }
                    if (params[0] != null) {
                        method.invoke(o, params);
                    }
                }
            }
        } catch (Throwable ex) {
            throw new DataSourceConfigurationException(ex);
        }
    }
}

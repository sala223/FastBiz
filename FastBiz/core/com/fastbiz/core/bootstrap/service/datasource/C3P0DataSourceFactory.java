package com.fastbiz.core.bootstrap.service.datasource;

import java.beans.PropertyVetoException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fastbiz.common.utils.StringUtils;
import com.fastbiz.core.bootstrap.service.datasource.cfg.DataSourceDefinition;
import com.fastbiz.core.bootstrap.service.datasource.cfg.NonTxDataSourceDefinition;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3P0DataSourceFactory implements DataSourceFactory{

    private static final Logger LOG = LoggerFactory.getLogger(C3P0DataSourceFactory.class);

    public Object createDataSource(DataSourceDefinition definition){
        if (definition instanceof NonTxDataSourceDefinition) {
            LOG.debug("Start creating DataSource pool with definition {}", definition);
            NonTxDataSourceDefinition nonTxDefinition = (NonTxDataSourceDefinition) definition;
            nonTxDefinition.validate();
            ComboPooledDataSource cpds = new ComboPooledDataSource();
            try {
                cpds.setDataSourceName(nonTxDefinition.getJndiName());
                cpds.setDriverClass(nonTxDefinition.getDriverClassName());
                cpds.setJdbcUrl(nonTxDefinition.getConnectionUrl());
                cpds.setUser(nonTxDefinition.getUserName());
                cpds.setPassword(nonTxDefinition.getPassword());
                if (nonTxDefinition.getMinPoolSize() > 0) {
                    cpds.setMinPoolSize(nonTxDefinition.getMinPoolSize());
                }
                if (nonTxDefinition.getMaxPoolSize() > 0) {
                    cpds.setMinPoolSize(nonTxDefinition.getMaxPoolSize());
                }
                if (nonTxDefinition.getMaxIdleTime() > 0) {
                    cpds.setMaxIdleTime(nonTxDefinition.getMaxIdleTime());
                }
                if (nonTxDefinition.getInitialPoolSize() > 0) {
                    cpds.setInitialPoolSize(nonTxDefinition.getInitialPoolSize());
                }
                if (nonTxDefinition.getAcquireRetryAttempts() > 0) {
                    cpds.setAcquireRetryAttempts(nonTxDefinition.getAcquireRetryAttempts());
                }
                if (nonTxDefinition.getBlockingTimeoutMillis() > 0) {
                    cpds.setCheckoutTimeout(nonTxDefinition.getBlockingTimeoutMillis());
                }
                if (nonTxDefinition.getPreparedStatementCacheSize() > 0) {
                    cpds.setMaxStatements(nonTxDefinition.getPreparedStatementCacheSize());
                }
                if (!StringUtils.isNullOrEmpty(nonTxDefinition.getCheckValidConnectionSql())) {
                    cpds.setPreferredTestQuery(nonTxDefinition.getCheckValidConnectionSql());
                }
                if (!StringUtils.isNullOrEmpty(nonTxDefinition.getValidConnectionCheckerClassName())) {
                    cpds.setConnectionTesterClassName(nonTxDefinition.getValidConnectionCheckerClassName());
                }
                Properties properties = definition.getProperties();
                cpds.setProperties(properties);
                return cpds;
            } catch (PropertyVetoException e) {
                throw new DataSourceException(e);
            }
        } else {
            throw new IllegalArgumentException("Can only accept argument type of " + NonTxDataSourceDefinition.class);
        }
    }
}

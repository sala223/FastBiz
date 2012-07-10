package com.fastbiz.core.bootstrap.service.datasource.cfg;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fastbiz.common.utils.StringUtils;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlRootElement
public abstract class DataSourceDefinition{

    public static enum TransactionIsolation {
        TRANSACTION_READ_UNCOMMITTED, TRANSACTION_READ_COMMITTED, TRANSACTION_REPEATABLE_READ, TRANSACTION_SERIALIZABLE,
        TRANSACTION_NONE
    }

    protected String               jndiName;

    protected String               connectionUrl;

    protected String               userName;

    protected String               password;

    protected TransactionIsolation transactionIsolation;

    protected String               checkValidConnectionSql;

    protected String               validConnectionCheckerClassName;

    protected int                  preparedStatementCacheSize;

    protected int                  queryTimeout;

    protected int                  maxPoolSize;

    protected int                  minPoolSize;

    protected int                  maxIdleTime;

    protected int                  acquireRetryAttempts;

    protected int                  blockingTimeoutMillis;

    protected int                  initialPoolSize;

    protected List<Property>       properties = new ArrayList<Property>();

    public Properties getProperties(){
        Properties p = new Properties();
        if (properties != null) {
            for (Property pro : this.properties) {
                p.setProperty(pro.getProperty(), pro.getValue());
            }
        }
        return p;
    }

    public void setProperty(String name, String value){
        properties.add(new Property(name, value));
    }

    public String getJndiName(){
        return jndiName;
    }

    @XmlElement(name = "jndi-name")
    public void setJndiName(String jndiName){
        this.jndiName = jndiName;
    }

    public int getInitialPoolSize(){
        return initialPoolSize;
    }

    @XmlElement(name = "initial-pool-size")
    public void setInitialPoolSize(int initialPoolSize){
        this.initialPoolSize = initialPoolSize;
    }

    public String getUserName(){
        return userName;
    }

    @XmlElement(name = "user-name")
    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getPassword(){
        return password;
    }

    @XmlElement(name = "password")
    public void setPassword(String password){
        this.password = password;
    }

    public TransactionIsolation getTransactionIsolation(){
        return transactionIsolation;
    }

    @XmlElement(name = "transaction-isolation")
    public void setTransactionIsolation(TransactionIsolation transactionIsolation){
        this.transactionIsolation = transactionIsolation;
    }

    public String getCheckValidConnectionSql(){
        return checkValidConnectionSql;
    }

    @XmlElement(name = "check-valid-connection-sql")
    public void setCheckValidConnectionSql(String checkValidConnectionSql){
        this.checkValidConnectionSql = checkValidConnectionSql;
    }

    public String getValidConnectionCheckerClassName(){
        return validConnectionCheckerClassName;
    }

    @XmlElement(name = "valid-connection-checker-class-name")
    public void setValidConnectionCheckerClassName(String validConnectionCheckerClassName){
        this.validConnectionCheckerClassName = validConnectionCheckerClassName;
    }

    public int getPreparedStatementCacheSize(){
        return preparedStatementCacheSize;
    }

    @XmlElement(name = "prepared-statement-cache-size")
    public void setPreparedStatementCacheSize(int preparedStatementCacheSize){
        this.preparedStatementCacheSize = preparedStatementCacheSize;
    }

    public int getQueryTimeout(){
        return queryTimeout;
    }

    @XmlElement(name = "query-Timeout")
    public void setQueryTimeout(int queryTimeout){
        this.queryTimeout = queryTimeout;
    }

    public int getMaxPoolSize(){
        return maxPoolSize;
    }

    @XmlElement(name = "max-pool-size")
    public void setMaxPoolSize(int maxPoolSize){
        this.maxPoolSize = maxPoolSize;
    }

    public int getMinPoolSize(){
        return minPoolSize;
    }

    @XmlElement(name = "min-pool-size")
    public void setMinPoolSize(int minPoolSize){
        this.minPoolSize = minPoolSize;
    }

    public int getMaxIdleTime(){
        return maxIdleTime;
    }

    @XmlElement(name = "max-idle-time")
    public void setMaxIdleTime(int maxIdleTime){
        this.maxIdleTime = maxIdleTime;
    }

    public int getAcquireRetryAttempts(){
        return acquireRetryAttempts;
    }

    @XmlElement(name = "acquire-retry-attempts")
    public void setAcquireRetryAttempts(int acquireRetryAttempts){
        this.acquireRetryAttempts = acquireRetryAttempts;
    }

    public int getBlockingTimeoutMillis(){
        return blockingTimeoutMillis;
    }

    @XmlElement(name = "block-timeout-millis")
    public void setBlockingTimeoutMillis(int blockingTimeoutMillis){
        this.blockingTimeoutMillis = blockingTimeoutMillis;
    }

    public String getConnectionUrl(){
        return connectionUrl;
    }

    @XmlElement(name = "connection-url")
    public void setConnectionUrl(String connectionUrl){
        this.connectionUrl = connectionUrl;
    }

    public void validate(){
        if (StringUtils.isNullOrEmpty(jndiName)) {
            throw new DataSourceConfigurationException("Data source JNDI Name cannot be empty");
        }
    }

    static class Property{

        private String property;

        private String value;

        public Property() {}

        public Property(String property, String value) {
            this.property = property;
            this.value = value;
        }

        public String getProperty(){
            return property;
        }

        public void setProperty(String property){
            this.property = property;
        }

        public String getValue(){
            return value;
        }

        public void setValue(String value){
            this.value = value;
        }
    }
}

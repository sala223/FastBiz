package com.fastbiz.core.bootstrap.service.datasource;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.CommonDataSource;
import javax.sql.DataSource;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fastbiz.core.tenant.TenantResolver;
import com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource;

public class DataSourceDelegator implements DataSource, XADataSource{

    public CommonDataSource     impl;

    private TenantResolver      tenantAware;

    private static final Logger LOG = LoggerFactory.getLogger(DataSourceDelegator.class);

    public DataSourceDelegator(TenantResolver tentantAware, CommonDataSource impl) {
        this.impl = impl;
    }

    public PrintWriter getLogWriter() throws SQLException{
        return impl.getLogWriter();
    }

    public void setLogWriter(PrintWriter out) throws SQLException{
        impl.setLogWriter(out);
    }

    public void setLoginTimeout(int seconds) throws SQLException{
        impl.setLoginTimeout(seconds);
    }

    public int getLoginTimeout() throws SQLException{
        return impl.getLoginTimeout();
    }

    public XAConnection getXAConnection() throws SQLException{
        return ((XADataSource) impl).getXAConnection();
    }

    public XAConnection getXAConnection(String user, String password) throws SQLException{
        return ((XADataSource) impl).getXAConnection(user, password);
    }

    public Connection getConnection() throws SQLException{
        return ((DataSource) impl).getConnection();
    }

    public Connection getConnection(String username, String password) throws SQLException{
        return ((DataSource) impl).getConnection(username, password);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException{
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException{
        return false;
    }

    public String getTenantId(){
        return tenantAware.getTenantId();
    }

    public void close(){
        if (impl != null) {
            if (impl instanceof AbstractPoolBackedDataSource) {
                ((AbstractPoolBackedDataSource) impl).close();
            } else {
                try {
                    Method method = impl.getClass().getMethod("close");
                    LOG.debug("Delegate to underly close method...");
                    method.invoke(impl);
                } catch (SecurityException ex) {
                    LOG.debug(ex.getMessage());
                } catch (NoSuchMethodException ex) {
                    LOG.debug(ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    LOG.debug(ex.getMessage());
                } catch (IllegalAccessException ex) {
                    LOG.debug(ex.getMessage());
                } catch (InvocationTargetException ex) {
                    LOG.debug(ex.getMessage());
                }
            }
        }
    }
}

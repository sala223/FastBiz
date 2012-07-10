package com.fastbiz.core.bootstrap.service.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.CommonDataSource;
import javax.sql.DataSource;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import com.fastbiz.core.tenant.TenantResolver;

public class DataSourceDelegator implements DataSource, XADataSource{

    public CommonDataSource impl;

    private TenantResolver     tenantAware;

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
}

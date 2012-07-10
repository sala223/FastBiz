package com.fastbiz.core.bootstrap.service.datasource;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ManagedDataSource implements DataSource{

    private DataSource          ds;
    private static final Logger log = LoggerFactory.getLogger(ManagedDataSource.class);

    public ManagedDataSource(DataSource ds) {
        this.ds = ds;
    }

    public void shutDown(){
        String warnMessage = "Shutdown datasource failed, resource leak might happen";
        try {
            Method method = ds.getClass().getMethod("close", new Class<?>[] {});
            method.invoke(ds, new Object[] {});
        } catch (SecurityException e) {
            log.warn(warnMessage, e);
        } catch (NoSuchMethodException e) {
            log.info("No close method is provided for the {} implementation", ds.getClass().getName());
            log.warn(warnMessage);
        } catch (IllegalArgumentException e) {
            log.warn(warnMessage, e);
        } catch (IllegalAccessException e) {
            log.warn(warnMessage, e);
        } catch (InvocationTargetException e) {
            log.warn(warnMessage, e);
        }
    }

    public PrintWriter getLogWriter() throws SQLException{
        return ds.getLogWriter();
    }

    public void setLogWriter(PrintWriter out) throws SQLException{
        ds.setLogWriter(out);
    }

    public void setLoginTimeout(int seconds) throws SQLException{
        ds.setLoginTimeout(seconds);
    }

    public int getLoginTimeout() throws SQLException{
        return ds.getLoginTimeout();
    }

    public <T> T unwrap(Class<T> iface) throws SQLException{
        return ds.unwrap(iface);
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException{
        return ds.isWrapperFor(iface);
    }

    public Connection getConnection() throws SQLException{
        return ds.getConnection();
    }

    public Connection getConnection(String username, String password) throws SQLException{
        return ds.getConnection(username, password);
    }
}

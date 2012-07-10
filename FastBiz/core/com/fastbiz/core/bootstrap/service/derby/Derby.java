package com.fastbiz.core.bootstrap.service.derby;

import java.io.File;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fastbiz.common.utils.FileUtils;
import com.fastbiz.core.bootstrap.Application;
import com.fastbiz.core.bootstrap.EnvironmentConfigration;
import com.fastbiz.core.bootstrap.service.BootstrapServiceBase;
import com.fastbiz.core.bootstrap.service.BootstrapServiceEvent;
import com.fastbiz.core.bootstrap.service.BootstrapServiceException;

public class Derby extends BootstrapServiceBase{

    public static final String  DERBY_DERIVER_CLASS_NAME      = "org.apache.derby.jdbc.EmbeddedDriver";

    private static final String DERBY_CONNECTION_URL_PATTERN  = "jdbc:derby:%s;create=true";

    private static final String SHUTDOWN_DERBY_CONNECTION_URL = "jdbc:derby:;shutdown=true";

    private static final String INIT_DB_NAME                  = "fastbiz";

    private static final Logger LOG                           = LoggerFactory.getLogger(Derby.class);

    @Override
    public void init(Application application){
        String derbyHome = FileUtils.newFilePath(EnvironmentConfigration.getBaseDir(), "data", "derby");
        System.setProperty("derby.system.home", derbyHome);
        try {
            FileUtils.ensureDirectory(new File(derbyHome));
            Class.forName(DERBY_DERIVER_CLASS_NAME);
        } catch (ClassNotFoundException ex) {
            throw new BootstrapServiceException(Derby.class.getName(), ex);
        } catch (IOException ex) {
            String fmt = new String("Unable to make derby directory %s");
            throw new BootstrapServiceException(Derby.class.getName(), ex, fmt, derbyHome);
        }
    }

    @Override
    public void start(Application application){
        try {
            DriverManager.getConnection(String.format(DERBY_CONNECTION_URL_PATTERN, INIT_DB_NAME));
        } catch (SQLException ex) {
            throw new BootstrapServiceException(Derby.class.getName(), ex);
        }
    }

    @Override
    public void stop(Application application){
        try {
            DriverManager.getConnection(SHUTDOWN_DERBY_CONNECTION_URL);
        } catch (SQLException ex) {
            if (!ex.getSQLState().equals("XJ015")) {
                LOG.info("Derby database shut down abnormal");
                this.fireServiceEvent(BootstrapServiceEvent.createStopWarningEvent(this, ex));
                return;
            }
        }
        LOG.info("Derby database shut down normally");
    }
}

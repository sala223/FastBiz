package com.fastbiz.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugUtils{

    private static Properties   debugProperties       = new Properties();

    private static boolean      isDebugEnabled        = false;

    private static final String DEBUG_PROPERTIES_FILE = "debug.properties";

    private static final Logger LOG                   = LoggerFactory.getLogger(DebugUtils.class);
    static {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(DEBUG_PROPERTIES_FILE);
        if (in != null) {
            isDebugEnabled = true;
            LOG.info("Debug switch is open, running in debug mode");
            try {
                debugProperties.load(in);
            } catch (IOException ex) {
                LOG.warn("Debug file {} is found, but cannot read it because of {}", DEBUG_PROPERTIES_FILE, ex.getMessage());
            }
        }
    }

    public static boolean isDebugEnabled(){
        return isDebugEnabled;
    }
}

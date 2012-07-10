package com.fastbiz.core.bootstrap;

import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Brand{

    private static Properties   brandProperties          = new Properties();

    private static final Logger LOG                      = LoggerFactory.getLogger(Brand.class);

    private static final String PRODUCT_CODE_PROPERTY    = "brand.product.code";

    private static final String PRODUCT_VERSION_PROPERTY = "brand.product.version";

    private static final String PRODUCT_COMPANY_PROPERTY = "brand.product.company";

    private static final String BRAND_PROPERTY_FILE      = "brand.properties";
    static {
        try {
            InputStream in = Brand.class.getClassLoader().getResourceAsStream(BRAND_PROPERTY_FILE);
            brandProperties.load(in);
        } catch (Throwable ex) {
            LOG.warn(String.format("Cannot fetch branding information from file %s", BRAND_PROPERTY_FILE), ex);
        }
    }

    public static String getProductCode(){
        return brandProperties.getProperty(PRODUCT_CODE_PROPERTY);
    }

    public static String getVersion(){
        return brandProperties.getProperty(PRODUCT_VERSION_PROPERTY);
    }

    public static String getCompany(){
        return brandProperties.getProperty(PRODUCT_COMPANY_PROPERTY);
    }
}

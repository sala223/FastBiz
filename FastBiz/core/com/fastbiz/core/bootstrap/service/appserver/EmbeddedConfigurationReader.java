package com.fastbiz.core.bootstrap.service.appserver;

import org.xml.sax.InputSource;

public interface EmbeddedConfigurationReader{

    EmbeddedConfiguration readEmbeddedConfiguration(InputSource source);
}

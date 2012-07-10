package com.fastbiz.core.bootstrap.service.appserver;

import org.xml.sax.InputSource;
import com.fastbiz.common.utils.xml.XmlUnmarshaller;

public class DefaultEmbeddedConfigurationReader implements EmbeddedConfigurationReader{

    @Override
    public EmbeddedConfiguration readEmbeddedConfiguration(InputSource source){
        XmlUnmarshaller<EmbeddedConfiguration> unmarshaller = new XmlUnmarshaller<EmbeddedConfiguration>(
                        EmbeddedConfiguration.class);
        return unmarshaller.unmarshall(source);
    }
}

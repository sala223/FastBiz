package com.fastbiz.common.utils.xml;

import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ClassPathEntityResolver implements EntityResolver{

    private static final String URL_SEPERATOR = "/";
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException{
        if (systemId != null) {
            int index = systemId.lastIndexOf(URL_SEPERATOR);
            int schemaFileNameStartIndex = 0;
            if (index != -1) {
                schemaFileNameStartIndex = index + 1;
            }
            String schemaFileName = systemId.substring(schemaFileNameStartIndex, systemId.length());
            Resource resource = new ClassPathResource(schemaFileName);
            InputSource source = new InputSource(resource.getInputStream());
            source.setPublicId(publicId);
            source.setSystemId(systemId);
            return source;
        }
        return null;
    }
}

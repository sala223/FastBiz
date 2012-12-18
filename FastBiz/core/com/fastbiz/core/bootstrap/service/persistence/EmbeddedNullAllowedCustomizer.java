package com.fastbiz.core.bootstrap.service.persistence;

import java.util.Vector;
import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.mappings.AggregateObjectMapping;
import org.eclipse.persistence.mappings.DatabaseMapping;

public class EmbeddedNullAllowedCustomizer implements DescriptorCustomizer{

    public void customize(ClassDescriptor descriptor) throws Exception{
        Vector<DatabaseMapping> mappings = descriptor.getMappings();
        for (DatabaseMapping mapping : mappings) {
            if (mapping instanceof AggregateObjectMapping) {
                ((AggregateObjectMapping) mapping).allowNull();
            }
        }
    }
}

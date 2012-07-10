package com.fastbiz.core.bootstrap.service.persistence;

import org.eclipse.persistence.config.DescriptorCustomizer;
import org.eclipse.persistence.descriptors.ClassDescriptor;

public class EntityCustomizer implements DescriptorCustomizer{

    @Override
    public void customize(ClassDescriptor descriptor) throws Exception{
        descriptor.getEventManager().addListener(new EntityTriggerListener());
    }
}

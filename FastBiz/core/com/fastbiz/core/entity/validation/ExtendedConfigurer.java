package com.fastbiz.core.entity.validation;

import net.sf.oval.configuration.Configurer;
import net.sf.oval.configuration.pojo.elements.ClassConfiguration;
import net.sf.oval.configuration.pojo.elements.ConstraintSetConfiguration;
import net.sf.oval.exception.InvalidConfigurationException;

public class ExtendedConfigurer implements Configurer{

    @Override
    public ClassConfiguration getClassConfiguration(Class<?> clazz) throws InvalidConfigurationException{
        return null;
    }

    @Override
    public ConstraintSetConfiguration getConstraintSetConfiguration(String constraintSetId)
                    throws InvalidConfigurationException{
        return null;
    }}

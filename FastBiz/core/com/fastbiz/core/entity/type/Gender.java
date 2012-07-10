package com.fastbiz.core.entity.type;

import org.eclipse.persistence.annotations.ConversionValue;
import org.eclipse.persistence.annotations.ObjectTypeConverter;

@ObjectTypeConverter(conversionValues = {
                @ConversionValue(dataValue = "M", objectValue = "Male"),
                @ConversionValue(dataValue = "F", objectValue = "Female") }, 
                dataType = String.class, name = "gender")
public enum Gender {
    male, female;
}

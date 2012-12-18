package com.fastbiz.core.entity.metadata;

import java.beans.PropertyDescriptor;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.BeanUtils;
import net.sf.oval.Check;
import net.sf.oval.constraint.MinLengthCheck;
import net.sf.oval.constraint.NotNullCheck;
import net.sf.oval.constraint.RangeCheck;

public enum ConstraintCheckType {
    NotNull(NotNullCheck.class), MaxLength(MinLengthCheck.class), Range(RangeCheck.class);

    private Class<? extends Check>        checkClass;

    private ConstraintCheckTypeDescriptor descriptor;

    private ConstraintCheckType(Class<? extends Check> checkClass) {
        this.checkClass = checkClass;
        this.descriptor = ConstraintCheckTypeDescriptor.getInstance(this);
    }

    public Class<? extends Check> getCheckClass(){
        return checkClass;
    }

    public ConstraintCheckTypeDescriptor getDescriptor(){
        return descriptor;
    }

    public static class ConstraintCheckTypeDescriptor{

        private PropertyDescriptor[]                                                descriptors;

        private static ConcurrentHashMap<ConstraintCheckType, PropertyDescriptor[]> properties;
        static {
            properties = new ConcurrentHashMap<ConstraintCheckType, PropertyDescriptor[]>();
        }

        public static ConstraintCheckTypeDescriptor getInstance(ConstraintCheckType checkType){
            checkType.getCheckClass();
            ConstraintCheckTypeDescriptor instance = new ConstraintCheckTypeDescriptor();
            PropertyDescriptor[] descriptors = properties.get(checkType);
            if (descriptors == null) {
                descriptors = BeanUtils.getPropertyDescriptors(checkType.getCheckClass());
                properties.putIfAbsent(checkType, descriptors);
            }
            instance.descriptors = descriptors;
            return instance;
        }

        public PropertyDescriptor[] getPropertyDescriptors(){
            return descriptors;
        }
    }
}

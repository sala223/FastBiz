package com.fastbiz.core.entity.metadata;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class ExtensibleEntityAttrGroup implements Serializable{

    private static final long                  serialVersionUID = 1L;

    private List<EntityExtendedAttrDescriptor> extendedAttributes;

    public ExtensibleEntityAttrTraverser getAttributeTraverser(){
        final Iterator<EntityExtendedAttrDescriptor> iterator = extendedAttributes.iterator();
        return new ExtensibleEntityAttrTraverser(){

            @Override
            public EntityExtendedAttrDescriptor nextAttribute(){
                if (iterator != null) {
                    if (iterator.hasNext()) {
                        iterator.next();
                    }
                    return null;
                } else {
                    return null;
                }
            }
        };
    }
}

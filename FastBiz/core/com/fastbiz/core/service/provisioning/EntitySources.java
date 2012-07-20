package com.fastbiz.core.service.provisioning;

import java.util.List;

public interface EntitySources{

    List<?> getEntitySet();

    boolean hasNext();
    
    List<EntityPostConstructor> getPostConstructors();
}

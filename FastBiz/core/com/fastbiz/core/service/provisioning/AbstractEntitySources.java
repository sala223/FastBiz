package com.fastbiz.core.service.provisioning;

import java.util.List;

public abstract class AbstractEntitySources implements EntitySources{

    private List<EntityPostConstructor> postConstructors;

    public void setPostConstructors(List<EntityPostConstructor> postConstructors){
        this.postConstructors = postConstructors;
    }

    @Override
    public List<EntityPostConstructor> getPostConstructors(){
        return postConstructors;
    }
}

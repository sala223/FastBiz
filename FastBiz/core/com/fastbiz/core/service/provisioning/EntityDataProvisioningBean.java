package com.fastbiz.core.service.provisioning;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.Assert;

public class EntityDataProvisioningBean extends TransactionalProvisioningBean implements TransactionalTasklet, InitializingBean{

    @PersistenceContext
    private EntityManager       entityManager;

    private EntitySources     entitySources;

    private List<?>             currentEntities;

    private static final Logger LOG = LoggerFactory.getLogger(EntityDataProvisioningBean.class);

    public EntityDataProvisioningBean() {}

    public EntityDataProvisioningBean(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setEntitySources(EntitySources entitySources){
        this.entitySources = entitySources;
    }

    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager(){
        return entityManager;
    }

    @Override
    protected boolean hasTransactionalTasklet(){
        return entitySources != null ? entitySources.hasNext() : false;
    }

    @Override
    protected TransactionalTasklet getTransactionalTasklet(){
        LOG.debug("Provisioning entity sources:" + this.entitySources);
        this.currentEntities = this.entitySources.getEntitySet();
        return this;
    }

    @Override
    public Object doInTransaction(TransactionStatus status){
        if (currentEntities == null || currentEntities.size() == 0) {
            return null;
        }
        List<EntityPostConstructor> postConstructors = entitySources.getPostConstructors();
        for (Object obj : currentEntities) {
            if (obj != null) {
                if (postConstructors != null) {
                    for (EntityPostConstructor constructor : postConstructors) {
                        if (constructor.supportEntity(obj)) {
                            constructor.processEntity(obj);
                        }
                    }
                }
                entityManager.merge(obj);
            }
        }
        return null;
    }

    @Override
    public void afterTransaction(){
        if (currentEntities != null) {
            currentEntities.clear();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception{
        Assert.notNull(entityManager, "entityManager must not be null");
        Assert.notNull(entitySources, "entitySources must not be null");
    }
}

package com.fastbiz.core.service.provisioning;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.Assert;
import com.fastbiz.common.utils.StringUtils;
import com.fastbiz.core.entity.MultiTenantSupport;
import com.fastbiz.core.tenant.TenantNotBoundException;
import com.fastbiz.core.tenant.TenantResolver;

public class EntityDataProvisioningBean extends TransactionalProvisioningBean implements InitializingBean{

    @PersistenceContext
    private EntityManager               entityManager;

    private EntitySources               entitySources;

    private TenantResolver              tenantResolver;
    
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

    public void setTenantResolver(TenantResolver tenantResolver){
        this.tenantResolver = tenantResolver;
    }

    @Override
    protected boolean hasTransactionalTasklet(){
        return entitySources != null ? entitySources.hasNext() : false;
    }

    @Override
    protected TransactionalTasklet getTransactionalTasklet(){
        LoadBulkEntitiesTasklet tasklet = new LoadBulkEntitiesTasklet();
        LOG.debug("Provisioning entity sources:" + this.entitySources);
        tasklet.setEntities(this.entitySources.getEntitySet());
        return tasklet;
    }

    class LoadBulkEntitiesTasklet implements TransactionalTasklet{

        private List<?> entities;

        public void setEntities(List<?> entities){
            this.entities = entities;
        }

        @Override
        public Object doInTransaction(TransactionStatus status){
            if (entities == null || entities.size() == 0) {
                return null;
            }
            String tenantId = tenantResolver.getTenantId();
            if (StringUtils.isNullOrEmpty(tenantId)) {
                throw new TenantNotBoundException("TenantId cannot be null or empty");
            }
            entityManager.setProperty(MultiTenantSupport.MULTITENANT_CONTEXT_PROPERTY, tenantId);
            List<EntityPostConstructor> postConstructors = entitySources.getPostConstructors();
            for (Object obj : entities) {
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
            entities.clear();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception{
        Assert.notNull(entityManager, "entityManager must not be null");
        Assert.notNull(tenantResolver, "tenantResolver must not be null");
    }
}

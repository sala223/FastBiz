package com.fastbiz.core.tenant;

import javax.persistence.EntityManager;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.Assert;
import com.fastbiz.common.utils.StringUtils;
import com.fastbiz.core.entity.MultiTenantSupport;

public class TenantInjectionInterceptor implements MethodInterceptor{

    private TenantResolver             tenantResolver;

    private PlatformTransactionManager transactionManager;
    
    private static final Logger LOG = LoggerFactory.getLogger(TenantInjectionInterceptor.class);

    public TenantInjectionInterceptor(PlatformTransactionManager tm, TenantResolver tenantResolver) {
        setTenantResolver(tenantResolver);
        setTransactionManager(tm);
    }

    public void setTenantResolver(TenantResolver tenantResolver){
        this.tenantResolver = tenantResolver;
    }

    public void setTransactionManager(PlatformTransactionManager transactionManager){
        Assert.notNull(transactionManager,"transactionManager is required, it must not not be null");
        if (JpaTransactionManager.class.isAssignableFrom(transactionManager.getClass())) {
            this.transactionManager = transactionManager;
        } else {
            throw new IllegalArgumentException("Only JpaTransactionManager is supported.");
        }
    }

    @Override
    public Object invoke(MethodInvocation invoation) throws Throwable{
        JpaTransactionManager tm = (JpaTransactionManager) transactionManager;
        Object resource = TransactionSynchronizationManager.getResource(tm.getEntityManagerFactory());
        EntityManagerHolder emHolder = (EntityManagerHolder) resource;
        if (emHolder != null) {
            EntityManager entityManager = emHolder.getEntityManager();
            String tenantId = tenantResolver.getTenantId();
            if (tenantId == null) {
                throw new TenantNotBoundException("TenantId is not bounded to current transaction");
            }
            Object value = entityManager.getProperties().get(MultiTenantSupport.MULTITENANT_CONTEXT_PROPERTY);
            if (value != null) {
                String newTenantId = value.toString();
                if (!StringUtils.isNullOrEmpty(newTenantId) && !tenantId.equals(newTenantId)) {
                    String fmt = "TenantId %s is bound to current transaction, you cannot change it to %s ";
                    throw new TenantNotBoundException(String.format(fmt, tenantId, newTenantId));
                }
            } else {
                LOG.debug("Inject TenantId {} to entity manager", tenantId);
                entityManager.setProperty(MultiTenantSupport.MULTITENANT_CONTEXT_PROPERTY, tenantId);
            }
        }
        return invoation.proceed();
    }
}

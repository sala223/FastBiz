package com.fastbiz.core.bootstrap.service.persistence;

import java.io.IOException;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.SessionEvent;
import org.eclipse.persistence.sessions.SessionEventAdapter;
import com.fastbiz.common.utils.StringUtils;
import com.fastbiz.core.entity.MultiTenantSupport;
import com.fastbiz.core.tenant.TenantAlreadyBoundException;
import com.fastbiz.core.tenant.TenantHolder;

public class TenantInjectionSessionEventListener extends SessionEventAdapter{

    @Override
    public void postAcquireClientSession(SessionEvent event){
        try {
            injectTenantProperty(event);
        } catch (IOException ex) {
            throw new PersistenceException(ex);
        }
    }

    @Override
    public void preBeginTransaction(SessionEvent event){
        try {
            injectTenantProperty(event);
        } catch (IOException ex) {
            throw new PersistenceException(ex);
        }
    }

    protected void injectTenantProperty(SessionEvent event) throws IOException{
        Session session = event.getSession();
        String tenantId = TenantHolder.getTenant();
        if (tenantId == null) {
            session.getLog().write("TenantId is not bounded to current session");
        }
        Object value = session.getProperties().get(MultiTenantSupport.MULTITENANT_CONTEXT_PROPERTY);
        if (value != null) {
            String newTenantId = value.toString();
            if (!StringUtils.isNullOrEmpty(newTenantId) && !tenantId.equals(newTenantId)) {
                String fmt = "TenantId %s is bound to current transaction, you cannot change it to %s ";
                throw new TenantAlreadyBoundException(String.format(fmt, tenantId, newTenantId));
            }
        } else {
            session.getLog().write("Inject TenantId " + tenantId + " to session");
            session.setProperty(MultiTenantSupport.MULTITENANT_CONTEXT_PROPERTY, tenantId);
        }
    }
}

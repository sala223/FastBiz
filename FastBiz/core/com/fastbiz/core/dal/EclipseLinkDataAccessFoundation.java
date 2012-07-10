package com.fastbiz.core.dal;

import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.descriptors.ClassDescriptor;
import org.eclipse.persistence.queries.FetchGroup;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.server.ServerSession;
import com.fastbiz.core.dal.exception.DataAccessException;
import com.fastbiz.core.dal.view.ViewId;

public class EclipseLinkDataAccessFoundation extends JPADataAccessFoundation{

    public void bulkInsert(List<?> objects){
        EntityManager em = getEntityManager();
        ServerSession serverSession = em.unwrap(ServerSession.class);
        serverSession.writeAllObjects(objects);
    }

    public Object findView(ViewId vid, Object id){
        String entityName = vid.getEntityName();
        String viewName = vid.getViewName();
        EntityManager em = getEntityManager();
        ClassDescriptor cd = getClassDescrptor(entityName);
        FetchGroup fg = cd.getFetchGroupManager().getFetchGroup(viewName);
        Vector<String> fields = cd.getPrimaryKeyFieldNames();
        if (fields.size() != 1) {
            String ft1 = "FindView only support the entity which doesnot have composite primary key,";
            String ft2 = "however, the primary key of entity %s is %s";
            throw new DataAccessException(ft1 + ft2, entityName, fields);
        }
        String eql = String.format("SELECT FROM %s o WHERE o.%s = :ID", cd.getPrimaryKeyFieldNames().get(0));
        Query query = em.createQuery(eql);
        query.setParameter("ID", id);
        query.setHint(QueryHints.FETCH_GROUP_NAME, fg);
        return query.getSingleResult();
    }

    public <T> List<T> queryView(ViewId vid, CriteriaQuery<T> query){
        String entityName = vid.getEntityName();
        String viewName = vid.getViewName();
        EntityManager em = getEntityManager();
        ClassDescriptor cd = getClassDescrptor(entityName);
        FetchGroup fg = cd.getFetchGroupManager().getFetchGroup(viewName);
        if (fg == null) {
            throw new DataAccessException("View %s is not defined in entity %s", viewName, entityName);
        }
        TypedQuery<T> tq = em.createQuery(query);
        tq.setHint(QueryHints.FETCH_GROUP_NAME, fg);
        return tq.getResultList();
    }

    protected ClassDescriptor getClassDescrptor(String entity){
        EntityManager em = getEntityManager();
        Session session = em.unwrap(Session.class);
        return session.getClassDescriptorForAlias(entity);
    }
}

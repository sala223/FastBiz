package com.fastbiz.core.dal;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import com.fastbiz.core.dal.exception.NativeQueryException;

public class JPADataAccessFoundation implements EntityManagerAware{

    private static final Pattern nativeSQLPattern = Pattern.compile("\\{\\?}|\\?");

    @PersistenceContext
    protected EntityManager      entityManager;

    public JPADataAccessFoundation() {}

    public JPADataAccessFoundation(EntityManager entityManager) {
        setEntityManager(entityManager);
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager(){
        return entityManager;
    }

    public <T> T find(Class<T> type, Object id){
        EntityManager em = getEntityManager();
        return em.find(type, id);
    }

    public void insert(Object object){
        EntityManager em = getEntityManager();
        em.persist(object);
    }

    public void update(Object object){
        EntityManager em = getEntityManager();
        em.merge(object);
    }

    public void remove(Object object){
        EntityManager em = getEntityManager();
        em.remove(object);
    }

    public void remove(Class<?> type, Object id){}

    public <T> T merge(T object){
        EntityManager em = getEntityManager();
        return em.merge(object);
    }

    public void flush(){
        EntityManager em = getEntityManager();
        em.flush();
    }

    public void bulkUpdate(List<?> objects){
        EntityManager em = getEntityManager();
        for (Object obj : objects) {
            em.merge(obj);
        }
    }

    public void bulkInsert(List<?> objects){
        EntityManager em = getEntityManager();
        for (Object obj : objects) {
            em.persist(obj);
        }
    }

    public List<?> queryByNamedQuery(String queryName, Map<String, ?> params){
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery(queryName);
        if (params != null) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        return query.getResultList();
    }

    public int deleteByNamedQuery(String queryName, Map<String, ?> params){
        return updateByNamedQuery(queryName, params);
    }

    public int updateByNamedQuery(String queryName, Map<String, ?> params){
        EntityManager em = getEntityManager();
        Query query = em.createNamedQuery(queryName);
        if (params != null) {
            for (String key : params.keySet()) {
                query.setParameter(key, params.get(key));
            }
        }
        return query.executeUpdate();
    }

    public <T> List<T> executeQuery(CriteriaQuery<T> query){
        EntityManager em = getEntityManager();
        return em.createQuery(query).getResultList();
    }

    public <T> T executeSingleQuery(CriteriaQuery<T> query){
        EntityManager em = getEntityManager();
        List<T> results = em.createQuery(query).getResultList();
        if (results != null && results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public <T> T executeSingleQuery(Query query){
        List<?> results = query.getResultList();
        if (results != null && results.size() > 0) {
            return (T) results.get(0);
        }
        return null;
    }

    public <T> CriteriaBuilder createQueryBuilder(){
        EntityManager em = getEntityManager();
        return em.getCriteriaBuilder();
    }

    public <T> T executeNativeQuery(SQLCallback<T> callback, String nativeSQL, Object ... parameters){
        EntityManager em = this.getEntityManager();
        Matcher matcher = nativeSQLPattern.matcher(nativeSQL);
        StringBuffer translatedSQL = new StringBuffer();
        int matchIndex = 0;
        while (matcher.find()) {
            String group = matcher.group();
            matchIndex++;
            if (parameters == null || parameters.length < matchIndex) {
                throw NativeQueryException.NativeSQLParValueNotSetException(nativeSQL, matchIndex);
            }
            Object parameterValue = parameters[matchIndex - 1];
            if (parameterValue == null) {
                throw NativeQueryException.NativeSQLParValueNotSetException(nativeSQL, matchIndex);
            }
            if (!group.startsWith("{")) {
                continue;
            }
            int size = 1;
            if (parameterValue.getClass().isArray()) {
                size = Array.getLength(parameterValue);
            } else if (parameterValue instanceof Collection) {
                size = ((Collection<?>) parameterValue).size();
            }
            if (size > 0) {
                StringBuffer expandedParameterList = new StringBuffer();
                for (int i = 0; i < size; i++) {
                    expandedParameterList.append("?");
                    if (i != size - 1) {
                        expandedParameterList.append(",");
                    }
                }
                matcher.appendReplacement(translatedSQL, expandedParameterList.toString());
            } else {
                matcher.appendReplacement(translatedSQL, "?");
            }
        }
        matcher.appendTail(translatedSQL);
        Query query = em.createNativeQuery(translatedSQL.toString());
        if (parameters != null) {
            int index = 1;
            for (int i = 0; i < parameters.length; ++i) {
                Object parameterValue = parameters[i];
                if (parameterValue.getClass().isArray()) {
                    int size = Array.getLength(parameterValue);
                    for (int j = 0; j < size; ++j) {
                        Object obj = Array.get(parameterValue, j);
                        query.setParameter(index, obj);
                        index++;
                    }
                } else if (parameterValue instanceof Collection) {
                    Object[] objs = ((Collection<?>) parameterValue).toArray();
                    int size = objs.length;
                    for (int j = 0; j < size; ++j) {
                        Object obj = objs[j];
                        query.setParameter(index, obj);
                        index++;
                    }
                } else {
                    query.setParameter(index, parameterValue);
                    index++;
                }
            }
        }
        List<?> resultList = query.getResultList();
        return callback.received(resultList);
    }
}

package com.fastbiz.core.dal;

import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public class JPADataAccessFoundation implements EntityManagerAware{

    @PersistenceContext
    protected EntityManager entityManager;

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

    public <T> CriteriaBuilder createQueryBuilder(){
        EntityManager em = getEntityManager();
        return em.getCriteriaBuilder();
    }
}

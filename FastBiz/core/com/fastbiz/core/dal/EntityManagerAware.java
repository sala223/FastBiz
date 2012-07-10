package com.fastbiz.core.dal;

import javax.persistence.EntityManager;

public interface EntityManagerAware{

    EntityManager getEntityManager();
}

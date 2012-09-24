package com.fastbiz.solution.wms.dal;

import java.util.List;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;
import com.fastbiz.core.dal.EclipseLinkDataAccessFoundation;
import com.fastbiz.solution.wms.entity.Category;
import com.fastbiz.solution.wms.entity.Constants;

@Repository
public class CategoryDAL extends EclipseLinkDataAccessFoundation implements Constants{

    public int disableCategoryRecursively(int categoryId){
        String eql = "UPDATE %s as c where c.id=:ID set c.isActive=:STATUS and c.children.isActive=:STATUS";
        Query query = this.getEntityManager().createQuery(String.format(eql, Constants.CATEGORY.CATEGORY_ENTITY_NAME));
        query.setParameter("ID", categoryId);
        query.setParameter("STATUS", false);
        return query.executeUpdate();
    }

    public int getChildrenSize(int categoryId){
        String eql = "SELECT COUNT(c.children.id) FROM %s as c where c.id=:ID";
        Query query = this.getEntityManager().createQuery(String.format(eql, Constants.CATEGORY.CATEGORY_ENTITY_NAME));
        query.setParameter("ID", categoryId);
        return (Integer) query.getSingleResult();
    }

    public Category findCategoryByName(String name){
        CriteriaBuilder builder = createQueryBuilder();
        CriteriaQuery<Category> query = builder.createQuery(Category.class);
        Root<Category> root = query.from(Category.class);
        query.where(builder.equal(root.get(CATEGORY.CATEGORY_NAME_PROPERTY), name));
        return executeSingleQuery(query);
    }
    
    public int removeCategoryByName(String categoryName){
        String eql = "DELETE FROM %s as c where c.name=:NAME";
        Query query = this.getEntityManager().createQuery(String.format(eql, Constants.CATEGORY.CATEGORY_ENTITY_NAME));
        query.setParameter("NAME", categoryName);
        return query.executeUpdate();
    }

    public List<Category> getCategoryChildren(int categoryId){
        CriteriaBuilder builder = createQueryBuilder();
        CriteriaQuery<Category> query = builder.createQuery(Category.class);
        Root<Category> root = query.from(Category.class);
        query.where(builder.equal(root.get(CATEGORY.CATEGORY_PARENT_ID_PROPERTY), categoryId));
        return this.executeQuery(query);
    }
}

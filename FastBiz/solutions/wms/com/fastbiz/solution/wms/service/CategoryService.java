package com.fastbiz.solution.wms.service;

import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fastbiz.solution.exception.CategoryException;
import com.fastbiz.solution.wms.dal.CategoryDAL;
import com.fastbiz.solution.wms.entity.Category;

@Service("categoryService")
@Transactional
public class CategoryService{

    @Autowired
    private CategoryDAL categoryDAL;

    @Autowired
    @Qualifier("VALIDATOR")
    protected Validator validator;

    public void deleteCategory(long categoryId){
        categoryDAL.remove(Category.class, categoryId);
    }

    public void removeCategoryByName(String categoryName){
        categoryDAL.removeCategoryByName(categoryName);
    }

    public void getCategoryChildren(long categoryId){
        categoryDAL.remove(Category.class, categoryId);
    }

    public void updateCategoryParent(long categoryId, long parentCategoryId){
        Category find = categoryDAL.find(Category.class, categoryId);
        if (find == null) {
            throw CategoryException.CategoryNonExistException(categoryId);
        }
        Category parent = categoryDAL.find(Category.class, categoryId);
        if (parent == null) {
            throw CategoryException.parentCategoryNotExistException(parentCategoryId);
        }
        find.setParent(parent);
        categoryDAL.merge(find);
    }

    public void updateCategoryParent(String categoryName, String parentCategoryName){
        Category find = categoryDAL.findCategoryByName(categoryName);
        if (find == null) {
            throw CategoryException.CategoryNonExistException(categoryName);
        }
        Category parent = categoryDAL.findCategoryByName(parentCategoryName);
        if (parent == null) {
            throw CategoryException.parentCategoryNotExistException(parentCategoryName);
        }
        find.setParent(parent);
        categoryDAL.merge(find);
    }

    @Transactional
    public void newCategory(Category category){
        validator.validate(category);
        Category find = categoryDAL.findCategoryByName(category.getName());
        if (find != null) {
            throw CategoryException.CategoryAlreadyExistException(category.getName());
        }
        Category parentValue = category.getParent();
        if (parentValue != null) {
            Category parent = categoryDAL.find(Category.class, parentValue.getId());
            if (parent == null) {
                throw CategoryException.parentCategoryNotExistException(parentValue.getId());
            }
        }
        categoryDAL.insert(category);
    }

    @Transactional
    public Category getCategoryByName(String categoryName){
        return categoryDAL.findCategoryByName(categoryName);
    }

    @Transactional
    public void disableCategory(long categoryId, boolean disableChildren){
        if (disableChildren) {
            categoryDAL.disableCategoryRecursively(categoryId);
        } else {
            int size = categoryDAL.getChildrenSize(categoryId);
            if (size != 0) {
                throw CategoryException.disableCategoryChildrenExistException();
            }
            categoryDAL.disableCategoryRecursively(categoryId);
        }
    }
}

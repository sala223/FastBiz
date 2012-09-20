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
public class CategoryService{

    @Autowired
    private CategoryDAL categoryDAL;

    @Autowired
    @Qualifier("VALIDATOR")
    protected Validator validator;

    public void deleteCategory(String categoryId){}

    @Transactional
    public void newCategory(Category category, int parentId){
        validator.validate(category);
        Category find = categoryDAL.findCategoryByName(category.getName());
        if (find != null) {
            throw CategoryException.CategoryAlreadyExistException();
        }
        if (parentId >= 0) {
            Category parent = categoryDAL.find(Category.class, parentId);
            if (parent == null) {
                throw CategoryException.parentCategoryNotExistException();
            }
            if (category.getChildren() != null) {
                category.getChildren().clear();
            }
            categoryDAL.insert(category);
            parent.getChildren().add(category);
            categoryDAL.update(parent);
        } else {
            if (category.getChildren() != null) {
                category.getChildren().clear();
            }
            categoryDAL.insert(category);
        }
    }

    @Transactional
    public void disableCategory(int categoryId, boolean disableChildren){
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

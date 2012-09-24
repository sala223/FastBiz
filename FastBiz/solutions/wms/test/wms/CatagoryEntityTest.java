package test.wms;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.fastbiz.solution.wms.entity.Category;
import com.fastbiz.solution.wms.service.CategoryService;

public class CatagoryEntityTest extends WMSTest{

    @Before
    public void initCategories(){
        CategoryService categoryService = solution.getBean(CategoryService.class);
        Category parent = new Category("root");
        parent.setDescription("root");
        Category node_1 = new Category("node_1");
        node_1.setDescription("node_1");
        Category node_2 = new Category("node_2");
        node_1.setDescription("node_2");
        Category node_3 = new Category("node_3");
        node_1.setDescription("node_3");
        Category node_3_1 = new Category("node_3_1");
        node_1.setDescription("node_3_1");
        categoryService.newCategory(parent);
        node_1.setParent(parent);
        node_2.setParent(parent);
        node_3.setParent(parent);
        parent = categoryService.getCategoryByName("root");
        categoryService.newCategory(node_1);
        categoryService.newCategory(node_2);
        categoryService.newCategory(node_3);
        node_3 = categoryService.getCategoryByName("node_3");
        node_3_1.setParent(node_3);
    }

    @After
    public void deleteCategories(){
        CategoryService categoryService = solution.getBean(CategoryService.class);
        categoryService.removeCategoryByName("node_3");
        categoryService.removeCategoryByName("node_2");
        categoryService.removeCategoryByName("node_1");
        categoryService.removeCategoryByName("node_3_1");
        categoryService.removeCategoryByName("root");
    }

    @Test
    public void testSetCategoryParent(){
        CategoryService categoryService = solution.getBean(CategoryService.class);
        categoryService.updateCategoryParent("node_3", "node_1");
        Category category = categoryService.getCategoryByName("node_3");
        TestCase.assertEquals(category.getParent().getName(), "node_1");
    }

    @Test
    public void testDisableCategory(){
        Category c = new Category("test1");
        c.setDescription("book");
        solution.getBean(CategoryService.class).newCategory(c);
    }
}

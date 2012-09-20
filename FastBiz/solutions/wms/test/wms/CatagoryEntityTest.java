package test.wms;

import org.junit.Test;
import com.fastbiz.solution.wms.entity.Category;
import com.fastbiz.solution.wms.service.CategoryService;

public class CatagoryEntityTest extends WMSTest{

    @Test
    public void testNewCategory(){
        Category parent = new Category("root");
        parent.setDescription("root");
        Category node_1 = new Category("node_1");
        node_1.setDescription("node_1");
        Category node_2 = new Category("node_2");
        node_1.setDescription("node_2");
        Category node_3 = new Category("node_3");
        node_1.setDescription("node_3");
        parent.getChildren().add(node_1);
        parent.getChildren().add(node_2);
        parent.getChildren().add(node_3);
        solution.getBean(CategoryService.class).newCategory(parent, -1);
    }
    
    @Test
    public void testDisableCategory(){
        Category c = new Category("test1");
        c.setDescription("book");
        solution.getBean(CategoryService.class).newCategory(c, -1);
    }
}

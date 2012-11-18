package test.entity;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.fastbiz.core.entity.extension.service.EntityExtensionService;
import com.fastbiz.core.entity.metadata.ConstraintCheckType;
import com.fastbiz.core.entity.metadata.EntityAttrType;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrConstraint;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrConstraintSet;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrDescriptor;
import com.fastbiz.core.testsuite.CoreTests;
import com.fastbiz.solution.idm.entity.User;

public class EntityExtensionTestCase extends CoreTests{

    @Autowired
    private EntityExtensionService service;

    @Test
    public void testCreateAndDeleteEntityAttribute(){
        EntityExtendedAttrDescriptor attr1 = new EntityExtendedAttrDescriptor("ext1", EntityAttrType.STRING);
        EntityExtendedAttrDescriptor attr2 = new EntityExtendedAttrDescriptor("ext2", EntityAttrType.STRING);
        List<EntityExtendedAttrDescriptor> attrs = new ArrayList<EntityExtendedAttrDescriptor>();
        attrs.add(attr1);
        attrs.add(attr2);
        service.addEntityAttributes(User.class, attrs);
        attrs = service.getExtendedAttributes(User.class);
        TestCase.assertEquals(attrs.size(), 2);
        service.deleteEntityAttributes(User.class, "ext1", "ext2");
        attrs = service.getExtendedAttributes(User.class);
        TestCase.assertEquals(attrs.size(), 0);
    }
    
    @Test
    public void testCreateEntityAttributeConstraint(){
        EntityExtendedAttrDescriptor attr1 = new EntityExtendedAttrDescriptor("ext1", EntityAttrType.STRING);
        EntityExtendedAttrDescriptor attr2 = new EntityExtendedAttrDescriptor("ext2", EntityAttrType.STRING);
        EntityExtendedAttrConstraint constraint1 = new EntityExtendedAttrConstraint("cons1",ConstraintCheckType.NotNull);
        EntityExtendedAttrConstraint constraint2 = new EntityExtendedAttrConstraint("cons2",ConstraintCheckType.MaxLength);
        attr1.getConstraintSet().addConstraint(constraint1);
        attr1.getConstraintSet().addConstraint(constraint2);
        attr2.getConstraintSet().addConstraint(constraint2);
        List<EntityExtendedAttrDescriptor> attrs = new ArrayList<EntityExtendedAttrDescriptor>();
        attrs.add(attr1);
        attrs.add(attr2);
        service.addEntityAttributes(User.class, attrs);
        attrs = service.getExtendedAttributes(User.class);
        TestCase.assertEquals(attrs.size(), 2);
        service.deleteEntityAttributes(User.class, "ext2");
        attr1 = service.getExtendedAttribute(User.class, "ext1");
        EntityExtendedAttrConstraintSet constraintSet = attr1.getConstraintSet();
        TestCase.assertEquals(constraintSet.getConstraintSize(), 2);
        service.deleteEntityAttributes(User.class, "ext1");
    }
}

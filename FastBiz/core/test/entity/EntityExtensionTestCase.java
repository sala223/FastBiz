package test.entity;

import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.fastbiz.core.entity.extension.service.contract.IEntityExtensionService;
import com.fastbiz.core.entity.metadata.ConstraintCheckType;
import com.fastbiz.core.entity.metadata.EntityAttrType;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrConstraint;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrConstraint.ConstraintParameter;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrDescriptor;
import com.fastbiz.core.testsuite.CoreTests;
import com.fastbiz.solution.idm.entity.Constants;

public class EntityExtensionTestCase extends CoreTests{

    @Autowired
    private IEntityExtensionService service;

    @Test
    public void testCreateAndDeleteEntityAttribute(){
        EntityExtendedAttrDescriptor attr1 = new EntityExtendedAttrDescriptor("ext1", EntityAttrType.STRING);
        EntityExtendedAttrDescriptor attr2 = new EntityExtendedAttrDescriptor("ext2", EntityAttrType.STRING);
        service.addEntityAttributes(Constants.USER.USER_ENTITY_NAME, attr1, attr2);
        List<EntityExtendedAttrDescriptor> attrs = service.getExtendedAttributes(Constants.USER.USER_ENTITY_NAME);
        TestCase.assertEquals(attrs.size(), 2);
        service.deleteEntityAttributes(Constants.USER.USER_ENTITY_NAME, attr1.getName(),attr2.getName());
        attrs = service.getExtendedAttributes(Constants.USER.USER_ENTITY_NAME);
        TestCase.assertEquals(attrs.size(), 0);
    }

    @Test
    public void testCreateEntityAttributeConstraint(){
        EntityExtendedAttrDescriptor attr1 = new EntityExtendedAttrDescriptor("ext21", EntityAttrType.STRING);
        EntityExtendedAttrDescriptor attr2 = new EntityExtendedAttrDescriptor("ext22", EntityAttrType.STRING);
        EntityExtendedAttrDescriptor attr3 = new EntityExtendedAttrDescriptor("ext23", EntityAttrType.DECIMAL);
        EntityExtendedAttrConstraint constraint1 = new EntityExtendedAttrConstraint("cons1", ConstraintCheckType.NotNull);
        EntityExtendedAttrConstraint constraint2 = new EntityExtendedAttrConstraint("cons2", ConstraintCheckType.MaxLength);
        EntityExtendedAttrConstraint constraint3 = new EntityExtendedAttrConstraint("cons3", ConstraintCheckType.Range);
        constraint2.addParameter("value", 11);
        constraint3.addParameter("min", 12);
        constraint3.addParameter("max", 100);
        attr1.addConstraint(constraint1);
        attr1.addConstraint(constraint2);
        attr2.addConstraint(constraint2);
        attr3.addConstraint(constraint3);
        service.addEntityAttributes(Constants.USER.USER_ENTITY_NAME, attr1, attr2, attr3);
        List<EntityExtendedAttrDescriptor> attrs = service.getExtendedAttributes(Constants.USER.USER_ENTITY_NAME);
        TestCase.assertEquals(attrs.size(), 3);
        service.deleteEntityAttributes(Constants.USER.USER_ENTITY_NAME, "ext22");
        attr1 = service.getExtendedAttribute(Constants.USER.USER_ENTITY_NAME, "ext21");
        TestCase.assertEquals(attr1.getConstraintSize(), 2);
        attr3 = service.getExtendedAttribute(Constants.USER.USER_ENTITY_NAME, "ext23");
        TestCase.assertEquals(attr3.getConstraintSize(), 1);
        Map<String, ConstraintParameter> parameters = attr3.getConstraints().get(0).getParameters();
        TestCase.assertEquals(attr3.getConstraints().get(0).getCheckType(),ConstraintCheckType.Range);
        TestCase.assertEquals(parameters.size(), 2);
        TestCase.assertEquals(parameters.get("min").getValue(), "12");
        TestCase.assertEquals(parameters.get("max").getValue(), "100");
        service.deleteEntityAttributes(Constants.USER.USER_ENTITY_NAME, "ext1", "ext22");
    }

    public void testUpdateEntityExtendedAttrDescriptor(){
        EntityExtendedAttrDescriptor attr1 = new EntityExtendedAttrDescriptor("ext1", EntityAttrType.STRING);
        EntityExtendedAttrConstraint constraint1 = new EntityExtendedAttrConstraint("cons1", ConstraintCheckType.NotNull);
        EntityExtendedAttrConstraint constraint2 = new EntityExtendedAttrConstraint("cons2", ConstraintCheckType.MaxLength);
        EntityExtendedAttrConstraint constraint3 = new EntityExtendedAttrConstraint("cons3", ConstraintCheckType.Range);
        constraint2.addParameter("value", 11);
        constraint3.addParameter("min", 12);
        constraint3.addParameter("max", 100);
        attr1.addConstraint(constraint1);
        attr1.addConstraint(constraint2);
        attr1.addConstraint(constraint3);
        service.addEntityAttributes(Constants.USER.USER_ENTITY_NAME, attr1);
        attr1 = service.getExtendedAttribute(Constants.USER.USER_ENTITY_NAME, "ext1");
    }
}

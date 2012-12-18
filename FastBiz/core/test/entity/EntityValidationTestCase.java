package test.entity;

import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.fastbiz.core.entity.extension.service.contract.IEntityExtensionService;
import com.fastbiz.core.entity.metadata.ConstraintCheckType;
import com.fastbiz.core.entity.metadata.EntityAttrType;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrConstraint;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrDescriptor;
import com.fastbiz.core.entity.validation.EntityExtendedAttrFactory;
import com.fastbiz.core.testsuite.CoreTests;
import com.fastbiz.solution.idm.entity.Constants;
import com.fastbiz.solution.idm.entity.User;

public class EntityValidationTestCase extends CoreTests{

    @Autowired
    private IEntityExtensionService   extensionService;

    @Autowired
    private EntityExtendedAttrFactory attrFactory ;

    @Test
    public void testEntityExtendedAttrFactory(){
        EntityExtendedAttrDescriptor attr1 = new EntityExtendedAttrDescriptor("ext21", EntityAttrType.STRING);
        EntityExtendedAttrDescriptor attr2 = new EntityExtendedAttrDescriptor("ext22", EntityAttrType.STRING);
        EntityExtendedAttrConstraint constraint1 = new EntityExtendedAttrConstraint("cons1", ConstraintCheckType.NotNull);
        EntityExtendedAttrConstraint constraint2 = new EntityExtendedAttrConstraint("cons2", ConstraintCheckType.MaxLength);
        EntityExtendedAttrConstraint constraint3 = new EntityExtendedAttrConstraint("cons3", ConstraintCheckType.Range);
        constraint2.addParameter("value", 11);
        constraint3.addParameter("min", 12);
        constraint3.addParameter("max", 100);
        attr1.addConstraint(constraint1);
        attr1.addConstraint(constraint2);
        attr1.addConstraint(constraint3);
        attr2.addConstraint(constraint1);
        attr2.addConstraint(constraint2);
        attr2.addConstraint(constraint3);
        extensionService.addEntityAttributes(Constants.USER.USER_ENTITY_NAME, attr1, attr2);
        EntityExtendedAttrDescriptor[] attrs = attrFactory.getEntityExtendedAttrs(User.class);
        TestCase.assertNotNull(attrs);
        TestCase.assertEquals(attrs.length, 2);
        extensionService.deleteEntityAttributes(Constants.USER.USER_ENTITY_NAME,attr1.getName(),attr2.getName());
    }
}

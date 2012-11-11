package test.entity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.Test;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrDescriptor;
import com.fastbiz.core.testsuite.CoreTests;


public class EntityExtensionTestCase extends CoreTests{
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Test
    public void testCreateEntityAttribute(){
        entityManager.find(EntityExtendedAttrDescriptor.class, 1);
    }
}

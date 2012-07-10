package test.idm.entity;

import org.junit.Test;
import com.fastbiz.core.entity.type.Gender;
import com.fastbiz.solution.idm.entity.User;
import com.fastbiz.solution.idm.service.UserManagementService;
import test.idm.IDMTest;

public class UserEntityTest extends IDMTest{

    @Test
    public void testInsert(){
        User user = new User();
        user.setCode("2222");
        user.setFirstName("Xia");
        user.setLastName("Pin");
        user.setPassword("ew3333");
        user.setGender(Gender.male);
        solution.getBean(UserManagementService.class).createUser(user);
    }

    @Test
    public void testUpdate(){}

    @Test
    public void testDelete(){}
}

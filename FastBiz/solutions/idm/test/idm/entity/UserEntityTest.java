package test.idm.entity;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.fastbiz.core.entity.type.Gender;
import com.fastbiz.solution.idm.entity.User;
import com.fastbiz.solution.idm.service.contract.IUserManagementService;
import test.idm.IDMTest;

public class UserEntityTest extends IDMTest{

    @Autowired
    private IUserManagementService userManagementService;

    @Test
    public void testInsert(){
        User user = new User();
        user.setCode("2222");
        user.setFirstName("Xia");
        user.setLastName("Pin");
        user.setPassword("ew3333");
        user.setGender(Gender.male);
        userManagementService.createUser(user);
    }

    @Test
    public void testUpdate(){}

    @Test
    public void testDelete(){}
}

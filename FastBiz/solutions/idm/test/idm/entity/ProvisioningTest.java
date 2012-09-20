package test.idm.entity;

import java.util.Date;
import org.junit.Test;
import com.fastbiz.common.utils.json.JsonUtils;
import com.fastbiz.solution.idm.entity.User;
import test.idm.IDMTest;

public class ProvisioningTest extends IDMTest{

    @Test
    public void produceUserJsonTemplate(){
        User[] users = new User[2];
        users[0] = new User();
        users[1] = new User();
        users[0].setCreatedTime(new Date());
        users[0].addStringAttribute("test1", "test1");
        users[1].addStringAttribute("test2", "test2");
        System.out.println(JsonUtils.toJson(users));
    }
}

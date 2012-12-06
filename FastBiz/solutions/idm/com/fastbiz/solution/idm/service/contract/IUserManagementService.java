package com.fastbiz.solution.idm.service.contract;

import javax.jws.WebService;
import com.fastbiz.solution.idm.entity.User;

@WebService
public interface IUserManagementService{

    void createUser(User user);

    void updateUser(User user);

    void updatePassword(String code, String newPassword);

    User getUserByCode(String userCode);
}

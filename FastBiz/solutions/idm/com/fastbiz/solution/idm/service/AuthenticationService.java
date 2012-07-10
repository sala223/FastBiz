package com.fastbiz.solution.idm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fastbiz.solution.idm.adapter.authentication.PasswordEncoder;
import com.fastbiz.solution.idm.dal.UserDAL;
import com.fastbiz.solution.idm.entity.User;
import com.fastbiz.solution.idm.exception.AuthenticationException;

@Service
public class AuthenticationService{

    @Autowired
    protected UserDAL         userDAL;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    public User authenticate(String code, String password){
        User user = userDAL.getUserByCode(code);
        if (user == null) {
            throw AuthenticationException.userNameNotFound();
        }
        boolean isValid = passwordEncoder.isPasswordValid(user.getPassword(), password, user.getSalt());
        if (!isValid) {
            throw AuthenticationException.badCredential();
        }
        return user;
    }
}

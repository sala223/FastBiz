package com.fastbiz.solution.idm.adapter.authentication;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.fastbiz.solution.idm.entity.User;
import com.fastbiz.solution.idm.service.UserManagementService;
import com.fastbiz.common.utils.Assert;

public class UserDetailsServiceImpl implements UserDetailsService, InitializingBean{

    @Autowired
    private UserManagementService userManagementService;

    public UserDetailsServiceImpl(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        User user = userManagementService.getUserByCode(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User %s is not found", username));
        }
        return new UserDetailsImpl(user);
    }

    @Override
    public void afterPropertiesSet() throws Exception{
        Assert.notNull(userManagementService, "userManagementService is required and canot be null");
    }
}
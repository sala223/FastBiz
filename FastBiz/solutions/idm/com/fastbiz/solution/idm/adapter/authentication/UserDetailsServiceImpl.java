package com.fastbiz.solution.idm.adapter.authentication;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.fastbiz.solution.idm.entity.User;
import com.fastbiz.solution.idm.service.contract.IUserManagementService;
import com.fastbiz.common.utils.Assert;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService, InitializingBean{

    @Autowired
    private IUserManagementService userManagementService;

    public UserDetailsServiceImpl() {}

    public UserDetailsServiceImpl(IUserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    public void setUserManagementService(IUserManagementService userManagementService){
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

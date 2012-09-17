package com.fastbiz.solution.idm.adapter.authentication;

import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("saltSource")
public class SaltSourceImpl implements SaltSource{

    @Override
    public Object getSalt(UserDetails user){
        if (user instanceof UserDetailsImpl) {
            UserDetailsImpl userImpl = (UserDetailsImpl) user;
            return userImpl.getUsername();
        } else {
            return "";
        }
    }
}

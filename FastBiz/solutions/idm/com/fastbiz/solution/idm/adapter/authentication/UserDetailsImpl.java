package com.fastbiz.solution.idm.adapter.authentication;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import com.fastbiz.solution.idm.entity.User;

public class UserDetailsImpl implements UserDetails{

    private static final long serialVersionUID = 1L;

    private User              user;

    public UserDetailsImpl(User user) {
        Assert.notNull(user);
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return null;
    }

    @Override
    public String getPassword(){
        return user.getPassword();
    }

    @Override
    public String getUsername(){
        return user.getCode();
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return !user.isLocked();
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return user.isEnabled();
    }
}

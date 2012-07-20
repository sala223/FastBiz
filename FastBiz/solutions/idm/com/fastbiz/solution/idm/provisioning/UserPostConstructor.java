package com.fastbiz.solution.idm.provisioning;

import org.springframework.util.Assert;
import com.fastbiz.core.service.provisioning.EntityPostConstructor;
import com.fastbiz.solution.idm.adapter.authentication.PasswordEncoder;
import com.fastbiz.solution.idm.entity.User;

public class UserPostConstructor implements EntityPostConstructor{

    private PasswordEncoder passwordEncoder;

    public UserPostConstructor() {}

    public UserPostConstructor(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void processEntity(Object obj){
        Assert.notNull(obj);
        User user = (User) obj;
        user.fillDefaultValue();
        user.setPassword(passwordEncoder.encodePassword(user.getPassword(), user.getSalt()));
    }

    @Override
    public boolean supportEntity(Object obj){
        if (obj != null) {
            return obj instanceof User;
        }
        return false;
    }
}

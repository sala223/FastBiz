package com.fastbiz.solution.idm.service;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fastbiz.core.validation.exception.ValidationException;
import com.fastbiz.solution.idm.adapter.authentication.PasswordEncoder;
import com.fastbiz.solution.idm.dal.UserDAL;
import com.fastbiz.solution.idm.entity.User;
import com.fastbiz.solution.idm.util.SolutionHelper;

@Transactional
@Service("userManagementService")
public class UserManagementService{

    @Autowired
    protected UserDAL         userDAL;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Autowired
    protected Validator       validator;

    public void setUserDAL(UserDAL userDAL){
        this.userDAL = userDAL;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    public void setValidator(Validator validator){
        this.validator = validator;
    }

    public void createUser(User user){
        user.fillDefaultValue();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (violations != null && violations.size() > 0) {
            throw new ValidationException(SolutionHelper.getSolutionId(), violations.toArray(new ConstraintViolation[0]));
        }
        user.setPassword(passwordEncoder.encodePassword(user.getPassword(), user.getSalt()));
        userDAL.createUser(user);
    }

    public void updateUser(User user){
        userDAL.update(user);
    }

    public void updatePassword(String code, String newPassword){
        User user = userDAL.getUserByCode(code);
        if (user != null) {
            String encodePassword = passwordEncoder.encodePassword(newPassword, user.getSalt());
            userDAL.updateUserPassword(code, encodePassword);
        }
    }

    public User getUserByCode(String userCode){
        return userDAL.getUserByCode(userCode);
    }
}

package com.fastbiz.idm.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.fastbiz.core.web.spring.security.controller.SecuredController;

public class UserManagementController implements SecuredController{

    @RequestMapping("/user/create")
    public void goToCreateUserPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("createUser");
    }
}

package com.fastbiz.core.web.spring.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

public class GateController implements SecuredController{

    @RequestMapping("/login")
    ModelAndView navigateToLoginPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }
}

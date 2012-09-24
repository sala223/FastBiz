package com.fastbiz.core.web.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GateController implements SecuredController{

    @RequestMapping("/login")
    ModelAndView navigateToLoginPage(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }
    
    @RequestMapping("/logout")
    ModelAndView logout(){
        SecurityContextHolder.clearContext();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("login");
        return mav;
    }
}

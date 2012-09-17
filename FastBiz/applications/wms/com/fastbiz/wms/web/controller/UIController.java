package com.fastbiz.wms.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.fastbiz.wms.web.model.ui.FunctionGroup;
import com.fastbiz.wms.web.service.IFunctionGroupService;

@Controller
@RequestMapping("/ui")
public class UIController{

    @Autowired
    private IFunctionGroupService functionGroupService;

    @RequestMapping("/functionGroup")
    public ModelAndView getSystemFunctionGroup(){
        FunctionGroup functionGroup = functionGroupService.getSystemFunctionGroup();
        ModelAndView mav = new ModelAndView();
        if (functionGroup != null) {
            mav.addObject(functionGroup);
        }
        return mav;
    }
}

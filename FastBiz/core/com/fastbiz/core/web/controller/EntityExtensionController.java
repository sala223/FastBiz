package com.fastbiz.core.web.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.fastbiz.core.entity.extension.service.EntityExtensionService;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrDescriptor;

@Controller
@RequestMapping("/extension")
public class EntityExtensionController{

    @Autowired
    private EntityExtensionService entityExtensionService;

    @RequestMapping("/{entity}/get")
    ModelAndView getEntityExtensionAttributes(@PathVariable("entity") String entity){
        ModelAndView mav = new ModelAndView();
        List<EntityExtendedAttrDescriptor> extendedAttributes = entityExtensionService.getExtendedAttributes(entity);
        mav.addObject(extendedAttributes);
        return mav;
    }
}

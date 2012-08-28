package com.fastbiz.wms.web.controller;

import java.util.ArrayList;
import java.util.Locale;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.fastbiz.core.locale.Language;

@Controller
@RequestMapping("/ui")
public class UIController{
    
    @RequestMapping("/functionGroups")
    public ModelAndView getFunctionGroup(){
        ArrayList<Language> languages = new ArrayList<Language>(); 
        languages.add(new Language(Locale.CHINESE.getLanguage(),Locale.CHINESE.getDisplayLanguage(Locale.CHINESE)));
        languages.add(new Language(Locale.ENGLISH.getLanguage(),Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH)));
        ModelAndView mav = new ModelAndView();
        mav.addObject(languages);
        return mav;   
    }
    
}

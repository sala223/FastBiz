package com.fastbiz.core.web.controller;

import java.util.ArrayList;
import java.util.Locale;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.fastbiz.core.locale.Language;

@Controller
@RequestMapping("/permitted/locale")
public class LocaleController implements PermittedController{
    
    @RequestMapping("/supportedLanguages")
    ModelAndView getSupportedLanguages(){
        ArrayList<Language> languages = new ArrayList<Language>(); 
        languages.add(new Language(Locale.CHINESE.getLanguage(),Locale.CHINESE.getDisplayLanguage(Locale.CHINESE)));
        languages.add(new Language(Locale.ENGLISH.getLanguage(),Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH)));
        ModelAndView mav = new ModelAndView();
        mav.addObject(languages);
        return mav;
    }
    
    @RequestMapping("/language")
    ModelAndView getCurrentLanguage(){
        Locale locale = LocaleContextHolder.getLocale();
        Language language = new Language(locale.getLanguage(),locale.getDisplayLanguage(locale));
        ModelAndView mav = new ModelAndView();
        mav.addObject(language);
        return mav;
    }
}

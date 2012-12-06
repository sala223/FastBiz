package com.fastbiz.core.web.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.springframework.context.i18n.LocaleContextHolder;
import com.fastbiz.common.utils.json.JsonUtils;
import com.fastbiz.core.locale.Language;

@Path("/locale")
@Produces({ "application/json" })
public class LocaleResource{

    @GET
    @Path("/supportedLanguages")
    public Map<String, List<Language>> getSupportedLanguages(){
        ArrayList<Language> languages = new ArrayList<Language>();
        languages.add(new Language(Locale.CHINESE.getLanguage(), Locale.CHINESE.getDisplayLanguage(Locale.CHINESE)));
        languages.add(new Language(Locale.ENGLISH.getLanguage(), Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH)));
        return JsonUtils.wrapList("languages", languages);
    }

    @GET
    @Path("/language")
    public Language getCurrentLanguage(){
        Locale locale = LocaleContextHolder.getLocale();
        return new Language(locale.getLanguage(), locale.getDisplayLanguage(locale));
    }
}

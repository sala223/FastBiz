package com.fastbiz.core.web.resources;

import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.fastbiz.core.entity.extension.service.EntityExtensionService;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrDescriptor;

@Component
@Path("/extension")
@Produces({ "application/json" })
public class ExtensionEntityResource{

    @Autowired
    private EntityExtensionService entityExtensionService;

    @RequestMapping("/{entity}/get")
    ModelAndView getEntityExtensionAttributes(@PathParam("entity") String entity){
        ModelAndView mav = new ModelAndView();
        List<EntityExtendedAttrDescriptor> extendedAttributes = entityExtensionService.getExtendedAttributes(entity);
        mav.addObject(extendedAttributes);
        return mav;
    }
}

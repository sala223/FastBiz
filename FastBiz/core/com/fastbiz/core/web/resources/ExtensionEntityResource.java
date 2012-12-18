package com.fastbiz.core.web.resources;

import java.util.List;
import java.util.Map;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import com.fastbiz.common.utils.json.JsonUtils;
import com.fastbiz.core.entity.extension.service.contract.IEntityExtensionService;
import com.fastbiz.core.entity.metadata.EntityExtendedAttrDescriptor;

@Path("/extension")
@Produces({ "application/json" })
public class ExtensionEntityResource{

    @Autowired
    private IEntityExtensionService entityExtensionService;

    @Path("/{entity}")
    @GET
    public Map<String, List<EntityExtendedAttrDescriptor>> getEntityExtensionAttributes(@PathParam("entity") String entity){
        List<EntityExtendedAttrDescriptor> extendedAttributes = entityExtensionService.getExtendedAttributes(entity);
        return JsonUtils.wrapList("entity", extendedAttributes);
    }

    @Path("/{entity}/add")
    @POST
    public void addEntityAttributes(@PathParam("entity") String entity,
                                    @FormParam(value = "attributes") EntityExtendedAttrDescriptor[] attributes){
        entityExtensionService.addEntityAttributes(entity, attributes);
    }

    @POST
    @Path("/{entity}/delete")
    public void deleteEntityAttributes(@PathParam("entity") String entity, @FormParam(value = "attributes") String[] attributes){
        entityExtensionService.deleteEntityAttributes(entity, attributes);
    }

    @POST
    @Path("/{entity}/update")
    public void updateEntityAttributes(@PathParam("entity") String entity,
                                       @FormParam(value = "attribute") EntityExtendedAttrDescriptor attr){
        entityExtensionService.updateEntityAttribute(attr);
    }
}

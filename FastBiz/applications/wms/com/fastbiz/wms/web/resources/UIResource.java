package com.fastbiz.wms.web.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fastbiz.wms.web.model.ui.FunctionGroup;
import com.fastbiz.wms.web.service.IFunctionGroupService;

@Component
@Path("/ui")
@Produces({ "application/json" })
public class UIResource{

    @Autowired
    private IFunctionGroupService functionGroupService;

    @GET
    @Path("/functionGroup")
    public FunctionGroup getSystemFunctionGroup(){
        FunctionGroup functionGroup = functionGroupService.getSystemFunctionGroup();
        return functionGroup;
    }
}

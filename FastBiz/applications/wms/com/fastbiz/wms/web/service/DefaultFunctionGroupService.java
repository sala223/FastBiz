package com.fastbiz.wms.web.service;

import java.util.List;
import com.fastbiz.wms.web.model.ui.FunctionGroup;

public class DefaultFunctionGroupService implements IFunctionGroupService {

    private List<FunctionGroupSource> sources;
    
    public void setSources(List<FunctionGroupSource> sources){
        this.sources = sources;
    }

    @Override
    public FunctionGroup getRootFunctionGroup(){
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FunctionGroup getFunctionGroup(String id){
        return null;
    }
}

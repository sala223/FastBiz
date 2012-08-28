package com.fastbiz.wms.web.service;

import com.fastbiz.wms.web.model.ui.FunctionGroup;

public class UIService{

    private IFunctionGroupService functionGroupService;

    public FunctionGroup getRootFunctionGroup(){
        return functionGroupService.getRootFunctionGroup();
    }

    public FunctionGroup getFunctionGroup(String functionGroup){
        return functionGroupService.getFunctionGroup(functionGroup);
    }
}

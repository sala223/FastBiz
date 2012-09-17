package com.fastbiz.wms.web.service;

import com.fastbiz.wms.web.model.ui.FunctionGroup;

public class SystemFunctionGroupSource implements FunctionGroupSource{

    private FunctionGroup functionGroup;

    public void setFunctionGroup(FunctionGroup functionGroup){
        this.functionGroup = functionGroup;
    }

    @Override
    public FunctionGroup getFunctionGroup(){
        return functionGroup;
    }
}

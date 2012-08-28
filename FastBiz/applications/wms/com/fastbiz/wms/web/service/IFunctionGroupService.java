package com.fastbiz.wms.web.service;

import com.fastbiz.wms.web.model.ui.FunctionGroup;

public interface IFunctionGroupService{

    public FunctionGroup getRootFunctionGroup();

    public FunctionGroup getFunctionGroup(String functionGroup);
}

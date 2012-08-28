package com.fastbiz.wms.web.model.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FunctionGroup{

    private String              id;

    private String              displayKey;

    private String              iconHref;

    private String              roleId;

    private List<FunctionGroup> subFunctionGroups = new ArrayList<FunctionGroup>();

    public FunctionGroup() {}

    public FunctionGroup(String id) {
        setId(id);
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getDisplayKey(){
        return displayKey;
    }

    public void setDisplayKey(String displayKey){
        this.displayKey = displayKey;
    }

    public String getIconHref(){
        return iconHref;
    }

    public void setIconHref(String iconHref){
        this.iconHref = iconHref;
    }

    public String getRoleId(){
        return roleId;
    }

    public void setRoleId(String roleId){
        this.roleId = roleId;
    }

    public List<FunctionGroup> getSubFunctionGroups(){
        return subFunctionGroups;
    }

    public void setSubFunctionGroups(List<FunctionGroup> subFunctionGroups){
        this.subFunctionGroups = subFunctionGroups;
    }

    public void addSubFunctionGroup(FunctionGroup ... functionGroups){
        if (functionGroups != null) {
            for (FunctionGroup functionGroup : functionGroups) {
                this.subFunctionGroups.add(functionGroup);
            }
        }
    }

    public List<FunctionGroup> getSubFunctionGroup(){
        return Collections.unmodifiableList(subFunctionGroups);
    }
}

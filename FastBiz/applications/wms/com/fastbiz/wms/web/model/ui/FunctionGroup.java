package com.fastbiz.wms.web.model.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.util.Assert;
import com.fastbiz.core.web.spring.security.access.BizResource;

@XmlRootElement
public class FunctionGroup implements BizResource{

    private static final long   serialVersionUID = 1L;

    private String              id;

    private String              display;

    private String              icon;

    @JsonIgnore
    private String              accessExpression;

    private String              url;

    private List<FunctionGroup> subs             = new ArrayList<FunctionGroup>();

    public FunctionGroup() {}

    public FunctionGroup(String id, String expression) {
        setId(id);
        setAccessExpression(expression);
    }

    public FunctionGroup(FunctionGroup copy) {
        setId(copy.getId());
        setIcon(copy.getIcon());
        setDisplay(copy.getDisplay());
        setUrl(copy.getUrl());
        setAccessExpression(copy.getAccessExpression());
        for (FunctionGroup sub : copy.getSubs()) {
            this.subs.add(new FunctionGroup(sub));
        }
    }

    public FunctionGroup findFunctionGroup(String id){
        Assert.notNull(id);
        if (this.getId().equals(id)) {
            return this;
        }
        for (FunctionGroup sub : subs) {
            FunctionGroup finded = sub.findFunctionGroup(id);
            if (finded != null) {
                return finded;
            }
        }
        return null;
    }

    public String getAccessExpression(){
        return accessExpression;
    }

    public void setAccessExpression(String accessExpression){
        this.accessExpression = accessExpression;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getDisplay(){
        return display;
    }

    public void setDisplay(String display){
        this.display = display;
    }

    public String getIcon(){
        return icon;
    }

    public void setIcon(String icon){
        this.icon = icon;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public List<FunctionGroup> getSubs(){
        ArrayList<FunctionGroup> copy = new ArrayList<FunctionGroup>();
        copy.addAll(subs);
        return Collections.unmodifiableList(copy);
    }

    public void setSubs(List<FunctionGroup> subs){
        this.subs = subs;
    }

    public void addSubFunctionGroup(FunctionGroup ... functionGroups){
        if (functionGroups != null) {
            for (FunctionGroup functionGroup : functionGroups) {
                this.subs.add(functionGroup);
            }
        }
    }

    public void removeSubFunctionGroup(String id){
        int index = -1;
        for (FunctionGroup sub : subs) {
            index++;
            if (sub.getId().equals(id)) {
                break;
            }
        }
        if (index != -1) {
            this.subs.remove(index);
        }
    }
}

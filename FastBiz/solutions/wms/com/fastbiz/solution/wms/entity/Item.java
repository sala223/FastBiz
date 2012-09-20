package com.fastbiz.solution.wms.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.eclipse.persistence.annotations.Cache;
import com.fastbiz.core.entity.MasterData;
import com.fastbiz.solution.idm.validation.IDMPayload;

@Cache
@Entity(name = "ITEM")
public class Item extends MasterData{

    @Column(length = 16, unique = true)
    @Size(message = "{item.code.Size}", min = 4, max = 32, payload = IDMPayload.class)
    @NotNull(message = "{item.code.NotNull}", payload = IDMPayload.class)
    private String         code;

    @Column(length = 56, unique = true)
    @Size(message = "{item.name.Size}", min = 4, max = 32, payload = IDMPayload.class)
    @NotNull(message = "{item.name.NotNull}", payload = IDMPayload.class)
    private String         name;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "ITEM_CATEGORY")
    private List<Category> Categories;

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public List<Category> getCategories(){
        return Categories;
    }

    public void setCategories(List<Category> categories){
        Categories = categories;
    }
}

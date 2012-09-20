package com.fastbiz.solution.wms.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.Index;
import org.eclipse.persistence.config.CacheIsolationType;
import com.fastbiz.core.entity.MultiTenantSupport;

@Cache(isolation = CacheIsolationType.SHARED)
@Entity(name = Constants.CATEGORY.CATEGORY_ENTITY_NAME)
@Table(name = Constants.CATEGORY.CATEGORY_ENTITY_TABLE)
public class Category extends MultiTenantSupport{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private long           id;

    @Column(length = 64)
    @Index
    private String         name;

    @Column
    private boolean        isEnabled = true;

    @Column(length = 512)
    private String         description;

    @OneToMany
    @JoinTable(name = "CATEGORY_HIEARCHY", joinColumns = { @JoinColumn(name = "PARENT") }, inverseJoinColumns = { @JoinColumn(name = "ID") })
    @BatchFetch(BatchFetchType.JOIN)
    private List<Category> children;

    public Category() {
        children = new ArrayList<Category>();
    }

    public Category(String name) {
        this();
        setName(name);
    }

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public boolean isEnabled(){
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled){
        this.isEnabled = isEnabled;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public List<Category> getChildren(){
        return children;
    }

    public void setChildren(List<Category> children){
        this.children = children;
    }
}

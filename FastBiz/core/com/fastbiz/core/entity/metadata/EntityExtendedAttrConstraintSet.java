package com.fastbiz.core.entity.metadata;

import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

@Embeddable
public class EntityExtendedAttrConstraintSet{

    @OneToMany
    @JoinTable(name = "ENTITY_EXT_ATTR_CONSTRAINT_SET", joinColumns = @JoinColumn(name = "ATTR_ID"), inverseJoinColumns = @JoinColumn(name = "CONSTRAINT_ID"))
    private List<EntityExtendedAttrConstraint> constraints;

    public List<EntityExtendedAttrConstraint> getConstraints(){
        return constraints;
    }

    public void setConstraints(List<EntityExtendedAttrConstraint> constraints){
        this.constraints = constraints;
    }
}

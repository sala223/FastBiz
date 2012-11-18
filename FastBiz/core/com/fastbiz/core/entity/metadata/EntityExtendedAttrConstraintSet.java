package com.fastbiz.core.entity.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import org.eclipse.persistence.annotations.CascadeOnDelete;

@Embeddable
public class EntityExtendedAttrConstraintSet{

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @CascadeOnDelete
    @JoinTable(name = "ENTITY_EXT_ATTR_CONSTRAINT_SET", joinColumns = @JoinColumn(name = "ATTR_ID"), inverseJoinColumns = @JoinColumn(name = "CONSTRAINT_ID"))
    private List<EntityExtendedAttrConstraint> constraints = new ArrayList<EntityExtendedAttrConstraint>();

    public List<EntityExtendedAttrConstraint> getConstraints(){
        return Collections.unmodifiableList(constraints);
    }

    public void setConstraints(List<EntityExtendedAttrConstraint> constraints){
        this.constraints = constraints;
    }

    public void addConstraint(EntityExtendedAttrConstraint constraint){
        constraints.add(constraint);
    }

    public int getConstraintSize(){
        return constraints.size();
    }
}

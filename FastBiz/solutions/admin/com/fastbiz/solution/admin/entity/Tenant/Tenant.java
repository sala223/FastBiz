package com.fastbiz.solution.admin.entity.Tenant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.eclipse.persistence.annotations.Index;

@Entity
@Index(name = "CODE_INDEX", unique = true, columnNames = { "CODE" })
public class Tenant{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private long   id;

    @Column(name = "ID")
    private String code;

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }
}

package com.fastbiz.core.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.eclipse.persistence.annotations.Index;

@MappedSuperclass
@Index(name = "UNIQUE_INDEX", unique = true, columnNames = { "CODE", MultiTenantSupport.TENANT_COLUMN })
public abstract class MasterData extends ExtensibleEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Temporal(value = TemporalType.TIME)
    @Column(nullable = false)
    private Date createdTime;

    @Temporal(value = TemporalType.TIME)
    private Date changedTime;

    @Column
    private long createdBy;

    @Temporal(value = TemporalType.DATE)
    private Date validFrom;

    @Temporal(value = TemporalType.DATE)
    private Date validTo;

    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public Date getCreatedTime(){
        return createdTime;
    }

    public void setCreatedTime(Date createdTime){
        this.createdTime = createdTime;
    }

    public Date getChangedTime(){
        return changedTime;
    }

    public void setChangedTime(Date changedTime){
        this.changedTime = changedTime;
    }

    public long getCreatedBy(){
        return createdBy;
    }

    public void setCreatedBy(long createdBy){
        this.createdBy = createdBy;
    }

    public Date getValidFrom(){
        return validFrom;
    }

    public void setValidFrom(Date validFrom){
        this.validFrom = validFrom;
    }

    public Date getValidTo(){
        return validTo;
    }

    public void setValidTo(Date validTo){
        this.validTo = validTo;
    }

    public void fillDefaultValue(){
        if (this.createdTime == null) {
            this.setCreatedTime(new Date());
        }
        if (this.validFrom == null) {
            this.setValidFrom(new Date());
        }
    }
}

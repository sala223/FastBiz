package com.fastbiz.solution.idm.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinTable;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.JoinFetch;
import org.eclipse.persistence.annotations.JoinFetchType;
import org.eclipse.persistence.config.CacheIsolationType;
import org.springframework.security.core.GrantedAuthority;
import com.fastbiz.core.entity.MultiTenantSupport;

@Entity(name = "ROLE")
@MappedSuperclass
@Cache(isolation = CacheIsolationType.SHARED)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonTypeInfo(use=JsonTypeInfo.Id.MINIMAL_CLASS, include=JsonTypeInfo.As.PROPERTY, property="@type")
public class Role extends MultiTenantSupport implements GrantedAuthority{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(initialValue = 5000, allocationSize = 1, name = "ROLE_SEQ")
    @Column(name = "ID")
    private long              id;

    @Column(length = 256)
    @Size(min = 1, max = 256)
    @NotNull
    private String            name;

    @Column(length = 256)
    @Size(min = 1, max = 256)
    private String            domain;

    @Column(length = 1025)
    @Size(min = 1, max = 256)
    @NotNull
    private String            description;

    @OneToMany
    @JoinFetch(value=JoinFetchType.INNER)
    @JoinTable(name = "ROLE_PERMISSION", joinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "ID"))
    private List<Permission>  permissions;

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

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public List<Permission> getPermissions(){
        return permissions;
    }

    public void setPermissions(List<Permission> permissions){
        this.permissions = permissions;
    }

    public String getDomain(){
        return domain;
    }

    public void setDomain(String domain){
        this.domain = domain;
    }

    @Override
    public String getAuthority(){
        return name;
    }
}

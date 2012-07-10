package com.fastbiz.solution.idm.entity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.eclipse.persistence.annotations.FetchGroup;
import org.eclipse.persistence.annotations.FetchAttribute;
import com.fastbiz.core.entity.MasterData;
import com.fastbiz.core.entity.type.Email;
import com.fastbiz.core.entity.type.Gender;
import com.fastbiz.core.entity.type.Image;
import com.fastbiz.solution.idm.validation.IDMPayload;

@Entity(name = "M_USER")
@FetchGroup(name = "AuthenticationInfo", attributes = { @FetchAttribute(name = "name"), @FetchAttribute(name = "password"),
                @FetchAttribute(name = "email"), @FetchAttribute(name = "telephone"), })
public class User extends MasterData{

    @Column(length = 32, unique = true)
    @Size(message = "{user.code.Size}", min = 4, max = 32, payload = IDMPayload.class)
    @NotNull(message = "{user.code.NotNull}", payload = IDMPayload.class)
    private String  code;

    @Column(length = 32)
    @Size(message = "{user.firstName.Size}", min = 1, max = 31, payload = IDMPayload.class)
    @NotNull(message = "{user.firstName.NotNull}", payload = IDMPayload.class)
    private String  firstName;

    @Column(length = 56)
    @Size(message = "{user.lastName.Size}", min = 1, max = 31, payload = IDMPayload.class)
    @NotNull(message = "{user.lastName.NotNull}", payload = IDMPayload.class)
    private String  lastName;

    @NotNull(message = "{user.password.NotNull}", payload = IDMPayload.class)
    @Size(message = "{user.password.Size}", min = 6, max = 30, payload = IDMPayload.class)
    @Column(length = 56)
    private String  password;

    private int     age;

    @Column(length = 20)
    private String  telephone;

    private Image   image;

    private Gender  gender;

    @Column(length = 12)
    private String  salt;

    @Embedded
    private Email   email;

    @Column
    private boolean enabled;

    @Column
    private boolean locked;

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public Email getEmail(){
        return email;
    }

    public void setEmail(Email email){
        this.email = email;
    }

    public String getTelephone(){
        return telephone;
    }

    public void setTelephone(String telephone){
        this.telephone = telephone;
    }

    public Image getImage(){
        return image;
    }

    public void setImage(Image image){
        this.image = image;
    }

    public int getAge(){
        return age;
    }

    public void setAge(int age){
        this.age = age;
    }

    public Gender getGender(){
        return gender;
    }

    public void setGender(Gender gender){
        this.gender = gender;
    }

    public String getSalt(){
        return salt;
    }

    public void setSalt(String salt){
        this.salt = salt;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public boolean isEnabled(){
        return enabled;
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
    }

    public boolean isLocked(){
        return locked;
    }

    public void setLocked(boolean locked){
        this.locked = locked;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }
}
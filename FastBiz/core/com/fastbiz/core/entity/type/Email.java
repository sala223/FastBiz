package com.fastbiz.core.entity.type;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Email implements Serializable{

    private static final long serialVersionUID = 1L;

    @Column(length = 128)
    private String            mail;

    public Email() {
        super();
    }

    public Email(String mail) {
        this.mail = mail;
    }

    public String getMail(){
        return mail;
    }

    public void setMail(String mail){
        this.mail = mail;
    }
}

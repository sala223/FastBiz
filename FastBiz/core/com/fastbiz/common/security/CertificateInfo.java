package com.fastbiz.common.security;

import java.io.Serializable;
import java.util.Date;

public class CertificateInfo implements Serializable{

    private static final long serialVersionUID = 1L;

    private long              serialNumber     = System.currentTimeMillis();

    private String            issuer;

    private String            subject;

    private String            signatureAlgorithm;

    private Date              validFrom;

    private Date              validTo;

    public String getIssuer(){
        return issuer;
    }

    public void setIssuer(String issuer){
        this.issuer = issuer;
    }

    public String getSubject(){
        return subject;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

    public long getSerialNumber(){
        return serialNumber;
    }

    public void setSerialNumber(long serialNumber){
        this.serialNumber = serialNumber;
    }

    public String getSignatureAlgorithm(){
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm){
        this.signatureAlgorithm = signatureAlgorithm;
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

    public String toString(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("{ serial number=" + serialNumber);
        buffer.append(", signature algorithm=" + signatureAlgorithm);
        buffer.append(", issuer=" + issuer);
        buffer.append(", subject=" + subject);
        buffer.append(", valid from=" + validFrom);
        buffer.append(", valid to=" + validTo + " }");
        return buffer.toString();
    }
}

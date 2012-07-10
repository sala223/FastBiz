package com.fastbiz.core.bootstrap.service.appserver.catalina;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.fastbiz.common.security.CertificateInfo;
import com.fastbiz.common.security.DataMarshaller;
import com.fastbiz.common.security.KeyInfo;
import com.fastbiz.common.security.KeyStoreInfo;
import com.fastbiz.core.bootstrap.service.appserver.EmbeddedSpec;

@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(name="catalina")
public class CatalinaSpec implements EmbeddedSpec{

    @XmlElement
    private KeyStoreInfo                    keyStore;

    @XmlElement
    private KeyInfo                         key;

    @XmlElement
    private CertificateInfo                 certificate;

    @XmlElement
    private String                          catalinaConfig;

    @XmlElement
    private String                          catalinaHome;

    @XmlElement
    private boolean                         exportCertificate;

    @XmlElement
    private boolean                         keyStoreCreate;

    @XmlElement
    private Class<? extends DataMarshaller> dataMarshallerType;

    private int                             httpPort;

    private int                             httpsPort;

    private static String                   STARTUP_CLASS_NAME = "com.fastbiz.core.bootstrap.service.appserver.catalina.CatalinaEmbedded";

    public KeyStoreInfo getKeyStore(){
        return keyStore;
    }

    public void setKeyStore(KeyStoreInfo keyStore){
        this.keyStore = keyStore;
    }

    public KeyInfo getKey(){
        return key;
    }

    public void setKey(KeyInfo key){
        this.key = key;
    }

    public CertificateInfo getCertificate(){
        return certificate;
    }

    public void setCertificate(CertificateInfo certificate){
        this.certificate = certificate;
    }

    public String getCatalinaHome(){
        return catalinaHome;
    }

    public void setCatalinaHome(String catalinaHome){
        this.catalinaHome = catalinaHome;
    }

    public boolean isExportCertificate(){
        return exportCertificate;
    }

    public void setExportCertificate(boolean exportCertificate){
        this.exportCertificate = exportCertificate;
    }

    public boolean isKeyStoreCreate(){
        return keyStoreCreate;
    }

    public void setKeyStoreCreate(boolean keyStoreCreate){
        this.keyStoreCreate = keyStoreCreate;
    }

    public String getCatalinaConfig(){
        return catalinaConfig;
    }

    public void setCatalinaConfig(String catalinaConfig){
        this.catalinaConfig = catalinaConfig;
    }

    public Class<? extends DataMarshaller> getDataMarshallerType(){
        return dataMarshallerType;
    }

    public void setDataMarshallerType(Class<? extends DataMarshaller> dataMarshallerType){
        this.dataMarshallerType = dataMarshallerType;
    }

    public int getHttpPort(){
        return httpPort;
    }

    public void setHttpPort(int httpPort){
        this.httpPort = httpPort;
    }

    public int getHttpsPort(){
        return httpsPort;
    }

    public void setHttpsPort(int httpsPort){
        this.httpsPort = httpsPort;
    }

    @Override
    public String getStartupClassName(){
        return STARTUP_CLASS_NAME;
    }
}

package com.fastbiz.core.bootstrap.service.appserver;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import com.fastbiz.core.bootstrap.service.appserver.catalina.CatalinaSpec;

@XmlRootElement(name = "embedded")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class EmbeddedConfiguration{

    @XmlElement(name = "scan")
    private List<Scan>  scans;

    @XmlElements(@XmlElement(name = "catalina", type = CatalinaSpec.class))
    public EmbeddedSpec spec;

    public int getHttpPort(){
        return spec.getHttpPort();
    }

    public int getHttpsPort(){
        return spec.getHttpsPort();
    }

    public EmbeddedSpec getSpec(){
        return spec;
    }

    public void setSpec(EmbeddedSpec spec){
        this.spec = spec;
    }

    public List<Scan> getScans(){
        return scans;
    }

    public void setScans(List<Scan> scans){
        this.scans = scans;
    }

    @XmlAccessorType(value = XmlAccessType.FIELD)
    public static class Scan{

        @XmlAttribute
        private String  location;

        @XmlAttribute
        private boolean isApplicationSet;

        public String getLocation(){
            return location;
        }

        public void setLocation(String location){
            this.location = location;
        }

        public boolean isApplicationSet(){
            return isApplicationSet;
        }

        public void setApplicationSet(boolean isApplicationSet){
            this.isApplicationSet = isApplicationSet;
        }
    }
}

package com.fastbiz.core.bootstrap.service.datasource.cfg;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fastbiz.common.utils.StringUtils;

@XmlRootElement(name="xa-datasource")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class XADataSourceDefinition extends DataSourceDefinition{

    private String xaDatasourceClassName;

    public String getXaDatasourceClassName(){
        return xaDatasourceClassName;
    }

    @XmlElement(name="xa-datasource-class-name")
    public void setXaDatasourceClassName(String xaDatasourceClassName){
        this.xaDatasourceClassName = xaDatasourceClassName;
    }

    public void validate(){
        super.validate();
        if (StringUtils.isNullOrEmpty(xaDatasourceClassName)) {
            throw new DataSourceConfigurationException("xaDatasourceClassName must be set for xa-datasource");
        }
    }
}

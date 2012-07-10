package com.fastbiz.core.bootstrap.service.datasource.cfg;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.fastbiz.common.utils.StringUtils;

@XmlRootElement(name = "no-tx-datasource")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class NonTxDataSourceDefinition extends DataSourceDefinition{

    private String driverClassName;

    public String getDriverClassName(){
        return driverClassName;
    }

    @XmlElement(name = "driver-class-name")
    public void setDriverClassName(String driverClassName){
        this.driverClassName = driverClassName;
    }

    public void validate(){
        super.validate();
        if (StringUtils.isNullOrEmpty(driverClassName)) {
            throw new DataSourceConfigurationException("driverClassName must be set for non-tx-datasource");
        }
    }
}

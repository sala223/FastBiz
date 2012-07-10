package com.fastbiz.core.bootstrap.service.datasource.cfg;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "datasources")
public class DataSourcesDefinition{

    @XmlElements(value = { @XmlElement(name = "xa-datasource", type = XADataSourceDefinition.class),
                    @XmlElement(name = "no-tx-datasource", type = NonTxDataSourceDefinition.class) })
    private List<DataSourceDefinition> dataSources = new ArrayList<DataSourceDefinition>();

    public DataSourceDefinition[] getAllDataSourceDefinition(){
        return dataSources.toArray(new DataSourceDefinition[dataSources.size()]);
    }

    public void addDataSourceDefinition(DataSourceDefinition dataSource){
        dataSources.add(dataSource);
    }
}

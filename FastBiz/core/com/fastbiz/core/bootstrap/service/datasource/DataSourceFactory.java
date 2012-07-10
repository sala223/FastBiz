package com.fastbiz.core.bootstrap.service.datasource;

import com.fastbiz.core.bootstrap.service.datasource.cfg.DataSourceDefinition;

public interface DataSourceFactory{

    public Object createDataSource(DataSourceDefinition element);
}

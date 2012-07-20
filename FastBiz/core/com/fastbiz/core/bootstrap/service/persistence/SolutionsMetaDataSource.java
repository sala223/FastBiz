package com.fastbiz.core.bootstrap.service.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Map;
import org.eclipse.persistence.internal.jpa.metadata.accessors.classes.EntityAccessor;
import org.eclipse.persistence.internal.jpa.metadata.xml.XMLEntityMappings;
import org.eclipse.persistence.internal.jpa.metadata.xml.XMLEntityMappingsReader;
import org.eclipse.persistence.jpa.metadata.XMLMetadataSource;
import org.eclipse.persistence.logging.SessionLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fastbiz.common.utils.ClassUtils;
import com.fastbiz.core.solution.SolutionBrowser;
import com.fastbiz.core.solution.StandardSolutionBrowser;

public class SolutionsMetaDataSource extends XMLMetadataSource{

    private SolutionBrowser     browser;

    private SolutionFilter      filter;

    private static final String SOLUTION_FILTER_PROP = "eclipselink.metadata-source-filter";

    private static final Logger LOG                  = LoggerFactory.getLogger(SolutionsMetaDataSource.class);

    public SolutionsMetaDataSource() {
        this.browser = new StandardSolutionBrowser();
    }

    @Override
    public XMLEntityMappings getEntityMappings(Map<String, Object> properties, ClassLoader classLoader, SessionLog log){
        String filterClassName = (String) properties.get(SOLUTION_FILTER_PROP);
        if (filterClassName != null) {
            try {
                Class<?> filterClass = ClassUtils.forName(filterClassName, null);
                if (SolutionFilter.class.isAssignableFrom(filterClass)) {
                    try {
                        this.filter = (SolutionFilter) filterClass.newInstance();
                    } catch (Throwable ex) {
                        String fmt = "Cannot instantiate solution filter class from class name %s";
                        LOG.warn(String.format(fmt,filterClassName),ex);
                    }
                }
            } catch (ClassNotFoundException ex) {
                String fmt = "Cannot load filter class from class name %s";
                LOG.warn(String.format(fmt,filterClassName),ex);
            } catch (LinkageError ex) {
                String fmt = "Cannot load filter class from class name %s";
                LOG.warn(String.format(fmt,filterClassName),ex);
            }
        }
        String[] solutionIds = browser.getSolutionIds();
        if (solutionIds.length == 0) {
            return null;
        }
        XMLEntityMappings target = getEntityMappings(properties, classLoader, solutionIds[0]);
        for (int i = 1; i < solutionIds.length; ++i) {
            XMLEntityMappings source = getEntityMappings(properties, classLoader, solutionIds[0]);
            if (target == null) {
                if (source != null) {
                    target = source;
                }
            } else {
                if (source != null) {
                    merge(source, target);
                }
            }
        }
        if (target != null) {
            for (EntityAccessor entity : target.getEntities()) {
                entity.setCustomizerClassName(EntityCustomizer.class.getName());
            }
        }
        return target;
    }

    public Reader getEntityMappingsReader(Map<String, Object> properties, ClassLoader classLoader, String solutionId){
        File solutionORMappingFile = browser.getEntityXmlORMappingFile(solutionId);
        if (solutionORMappingFile != null && solutionORMappingFile.exists()) {
            try {
                return new FileReader(solutionORMappingFile);
            } catch (FileNotFoundException ex) {
                String fmt = "Cannot find entity ORMapping file %s in solution %s";
                LOG.warn(String.format(fmt, solutionORMappingFile, solutionId), ex);
                return null;
            }
        } else {
            return null;
        }
    }

    public XMLEntityMappings getEntityMappings(Map<String, Object> properties, ClassLoader classLoader, String solutionId){
        if (filter != null) {
            if(!filter.accept(browser.getSolutionDescriptor(solutionId))){
                return null;
            }
        }
        Reader reader = getEntityMappingsReader(properties, classLoader, solutionId);
        if (reader == null) {
            return null;
        }
        try {
            return XMLEntityMappingsReader.read(getRepositoryName(), reader, classLoader, properties);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception ex) {}
            }
        }
    }

    protected void merge(XMLEntityMappings source, XMLEntityMappings target){
        target.getEntities().addAll(source.getEntities());
        target.getEmbeddables().addAll(source.getEmbeddables());
        target.getMappedSuperclasses().addAll(source.getMappedSuperclasses());
        target.getConverters().addAll(source.getConverters());
        target.getTypeConverters().addAll(source.getTypeConverters());
        target.getObjectTypeConverters().addAll(source.getObjectTypeConverters());
        target.getStructConverters().addAll(source.getStructConverters());
        target.getTableGenerators().addAll(source.getTableGenerators());
        target.getSequenceGenerators().addAll(source.getSequenceGenerators());
        target.getPartitioning().addAll(source.getPartitioning());
        target.getReplicationPartitioning().addAll(source.getReplicationPartitioning());
        target.getRoundRobinPartitioning().addAll(source.getRoundRobinPartitioning());
        target.getHashPartitioning().addAll(source.getHashPartitioning());
        target.getValuePartitioning().addAll(source.getValuePartitioning());
        target.getPinnedPartitioning().addAll(source.getPinnedPartitioning());
        target.getRangePartitioning().addAll(source.getRangePartitioning());
        target.getNamedNativeQueries().addAll(source.getNamedNativeQueries());
        target.getNamedQueries().addAll(source.getNamedQueries());
        target.getNamedStoredProcedureQueries().addAll(source.getNamedStoredProcedureQueries());
        target.getNamedStoredFunctionQueries().addAll(source.getNamedStoredFunctionQueries());
        target.getNamedPLSQLStoredProcedureQueries().addAll(source.getNamedPLSQLStoredProcedureQueries());
        target.getNamedPLSQLStoredFunctionQueries().addAll(source.getNamedPLSQLStoredFunctionQueries());
        target.getSqlResultSetMappings().addAll(source.getSqlResultSetMappings());
        target.getPLSQLRecords().addAll(source.getPLSQLRecords());
        target.getPLSQLTables().addAll(source.getPLSQLTables());
        target.getTenantDiscriminatorColumns().addAll(source.getTenantDiscriminatorColumns());
    }
}

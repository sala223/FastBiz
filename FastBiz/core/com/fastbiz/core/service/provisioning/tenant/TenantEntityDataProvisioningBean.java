package com.fastbiz.core.service.provisioning.tenant;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.TransactionStatus;
import com.fastbiz.core.entity.MultiTenantSupport;
import com.fastbiz.core.service.provisioning.DirectoryArchiveEntitySources;
import com.fastbiz.core.service.provisioning.EntityDataProvisioningBean;
import com.fastbiz.core.service.provisioning.EntityPostConstructor;
import com.fastbiz.core.service.provisioning.EntitySources;

public class TenantEntityDataProvisioningBean extends EntityDataProvisioningBean implements ApplicationContextAware{

    private File[]             tenantDirectories = new File[0];

    private int                currentIndex      = -1;

    private File               root;

    private ApplicationContext applicationContext;

    public TenantEntityDataProvisioningBean(File root) {
        this.root = root;
        initTenantDirectories();
    }

    protected void initTenantDirectories(){
        if (root != null && root.isDirectory()) {
            tenantDirectories = root.listFiles(new FileFilter(){

                @Override
                public boolean accept(File file){
                    return file.isDirectory();
                }
            });
            this.setEntitySources(new TenantEntitySources());
        } else {
            String fmt = "Directory %s does not exist or is not a directory";
            throw new IllegalArgumentException(String.format(fmt, root));
        }
    }

    @Override
    public Object doInTransaction(TransactionStatus status){
        this.getEntityManager().setProperty(MultiTenantSupport.MULTITENANT_CONTEXT_PROPERTY, getCurrentTenant());
        return super.doInTransaction(status);
    }

    File getNextTenantDirectory(){
        currentIndex++;
        return new File(tenantDirectories[currentIndex], this.getSolutionDescriptor().getSolutionId());
    }

    boolean hasNextTenant(){
        if (currentIndex < 0) {
            return tenantDirectories.length > 0;
        } else {
            return tenantDirectories.length > currentIndex + 1;
        }
    }

    protected String getCurrentTenant(){
        return tenantDirectories[currentIndex].getName();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException{
        this.applicationContext = applicationContext;
    }

    class TenantEntitySources implements EntitySources{

        private DirectoryArchiveEntitySources currentTenantEntitySource;

        @Override
        public List<?> getEntitySet(){
            return currentTenantEntitySource.getEntitySet();
        }

        @Override
        public boolean hasNext(){
            if (currentTenantEntitySource == null) {
                if (tenantDirectories == null || tenantDirectories.length == 0) {
                    return false;
                } else {
                    if (hasNextTenant()) {
                        currentTenantEntitySource = new DirectoryArchiveEntitySources(getNextTenantDirectory());
                        currentTenantEntitySource.setApplicationContext(applicationContext);
                        currentTenantEntitySource.initializeWithConfigurationFile();
                        return this.hasNext();
                    } else {
                        return false;
                    }
                }
            } else {
                if (currentTenantEntitySource.hasNext()) {
                    return true;
                }
                if (hasNextTenant()) {
                    currentTenantEntitySource = new DirectoryArchiveEntitySources(getNextTenantDirectory());
                    currentTenantEntitySource.setApplicationContext(applicationContext);
                    currentTenantEntitySource.initializeWithConfigurationFile();
                    return this.hasNext();
                } else {
                    return false;
                }
            }
        }

        @Override
        public List<EntityPostConstructor> getPostConstructors(){
            return currentTenantEntitySource.getPostConstructors();
        }
    }
}

package com.fastbiz.core.service.provisioning;

import org.springframework.transaction.support.TransactionTemplate;

public abstract class TransactionalProvisioningBean extends AbstractProvisioningBean{

    private TransactionTemplate template;

    protected TransactionTemplate getTransactionTemplate(){
        return template;
    }

    public void setTransactionTemplate(TransactionTemplate template){
        this.template = template;
    }

    protected abstract boolean hasTransactionalTasklet();

    protected abstract TransactionalTasklet getTransactionalTasklet();

    @Override
    public void run(ProvisioningContext context) throws Exception{
        while (this.hasTransactionalTasklet()) {
            TransactionalTasklet tasklet = this.getTransactionalTasklet();
            if (tasklet != null) {
                if (tasklet != null) {
                    template.execute(tasklet);
                }
            }
        }
    }
}

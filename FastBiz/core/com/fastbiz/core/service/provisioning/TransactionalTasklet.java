package com.fastbiz.core.service.provisioning;

import org.springframework.transaction.support.TransactionCallback;

public interface TransactionalTasklet extends TransactionCallback<Object>{

    void afterTransaction();
}

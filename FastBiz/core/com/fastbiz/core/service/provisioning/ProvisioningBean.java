package com.fastbiz.core.service.provisioning;

import org.springframework.core.Ordered;

public interface ProvisioningBean extends Ordered{

    void execute(ProvisioningContext context);
}

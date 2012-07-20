package com.fastbiz.core.service.provisioning;

import com.fastbiz.core.solution.SolutionDescriptorAware;
import com.fastbiz.core.solution.descriptor.SolutionDescriptor;

public abstract class AbstractProvisioningBean implements SolutionDescriptorAware, ProvisioningBean{

    private SolutionDescriptor  solutionDescriptor;

    private int                 order;

    @Override
    public void setSolutionDescriptor(SolutionDescriptor solutionDescriptor){
        this.solutionDescriptor = solutionDescriptor;
    }

    public SolutionDescriptor getSolutionDescriptor(){
        return solutionDescriptor;
    }

    public final void execute(ProvisioningContext context){
        try {
            run(context);
        } catch (Throwable ex) {
            context.addBeanRunningStatus(this.getClass(), ex);
        }
    }

    public void setOrder(int order){
        this.order = order;
    }

    @Override
    public int getOrder(){
        return this.order;
    }

    public abstract void run(ProvisioningContext context) throws Exception;
}

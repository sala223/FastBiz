package com.fastbiz.core.service.provisioning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import com.fastbiz.core.service.provisioning.ProvisioningContext.ProvisioningStatus;

public class ProvisioningService implements BeanPostProcessor, ApplicationListener<ContextRefreshedEvent>{

    private List<ProvisioningBean> beans = new ArrayList<ProvisioningBean>();

    private static final Logger    LOG   = LoggerFactory.getLogger(ProvisioningService.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException{
        if (bean instanceof ProvisioningBean) {
            beans.add((ProvisioningBean) bean);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException{
        return bean;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event){
        if (beans.size() == 0) {
            return;
        }
        Collections.sort(beans, new Comparator<ProvisioningBean>(){

            @Override
            public int compare(ProvisioningBean obj1, ProvisioningBean obj2){
                return obj1.getOrder() - obj2.getOrder();
            }
        });
        ProvisioningContext context = new ProvisioningContext();
        for (ProvisioningBean pb : beans) {
            pb.execute(context);
        }
        List<ProvisioningStatus> errorBeans = context.listBeansWithError();
        for (ProvisioningStatus errorBean : errorBeans) {
            String fmt = "Running provision bean %s with error %s";
            LOG.error(String.format(fmt, errorBean.getBeanClassName(), errorBean.getError().getMessage()), errorBean.getError());
        }
        beans.clear();
    }
}

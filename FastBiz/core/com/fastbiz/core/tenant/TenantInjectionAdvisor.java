package com.fastbiz.core.tenant;

import java.io.Serializable;
import java.lang.reflect.Method;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.transaction.interceptor.TransactionAttributeSource;
import org.springframework.util.ObjectUtils;

public class TenantInjectionAdvisor extends AbstractPointcutAdvisor{

    private static final long          serialVersionUID = 1L;

    private TenantInjectionInterceptor tenantInjectionInterceptor;

    private Pointcut                   pointcut;

    public TenantInjectionAdvisor() {
        this(null, null);
    }

    public TenantInjectionAdvisor(TenantInjectionInterceptor interceptor, TransactionAttributeSource transactionAttributeSource) {
        this.tenantInjectionInterceptor = interceptor;
        setTenantInjectionInterceptor(interceptor);
        setTransactionAttributeSource(transactionAttributeSource);
    }

    public void setTenantInjectionInterceptor(TenantInjectionInterceptor tenantInjectionInterceptor){
        this.tenantInjectionInterceptor = tenantInjectionInterceptor;
    }

    public void setTransactionAttributeSource(TransactionAttributeSource transactionAttributeSource){
        pointcut = new TenantInjectionPointcut(transactionAttributeSource);
    }

    @Override
    public Pointcut getPointcut(){
        return pointcut;
    }

    @Override
    public Advice getAdvice(){
        return tenantInjectionInterceptor;
    }

    final static class TenantInjectionPointcut extends StaticMethodMatcherPointcut implements Serializable{

        private static final long          serialVersionUID = 1L;

        private TransactionAttributeSource tas;

        public TenantInjectionPointcut(TransactionAttributeSource transactionAttributeSource) {
            this.tas = transactionAttributeSource;
        }

        public boolean matches(Method method, Class<?> targetClass){
            return (tas == null || tas.getTransactionAttribute(method, targetClass) != null);
        }

        @Override
        public boolean equals(Object other){
            if (this == other) {
                return true;
            }
            if (!(other instanceof TenantInjectionPointcut)) {
                return false;
            }
            TenantInjectionPointcut otherPc = (TenantInjectionPointcut) other;
            return ObjectUtils.nullSafeEquals(tas, otherPc.tas);
        }

        @Override
        public int hashCode(){
            return TenantInjectionPointcut.class.hashCode();
        }

        @Override
        public String toString(){
            return getClass().getName() + ": " + tas;
        }
    }
}

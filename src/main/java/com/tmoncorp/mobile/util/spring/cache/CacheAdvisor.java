package com.tmoncorp.mobile.util.spring.cache;

import java.lang.reflect.Method;

import com.tmoncorp.mobile.util.common.cache.Cache;
import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheAdvisor extends AbstractPointcutAdvisor {

	/**
	 * 
	 */
    private static final long serialVersionUID = -2695109968446054684L;
    
    private final StaticMethodMatcherPointcut pointcut = new
            StaticMethodMatcherPointcut() {
                @Override
                public boolean matches(Method method, Class<?> targetClass) {
                    return method.isAnnotationPresent(Cache.class);
                }
            };
            
    @Autowired
    private MCacheInterceptor interceptor;
    
	@Override
	public Pointcut getPointcut() {
		return pointcut;
	}

	@Override
	public Advice getAdvice() {
		return interceptor;
	}

}

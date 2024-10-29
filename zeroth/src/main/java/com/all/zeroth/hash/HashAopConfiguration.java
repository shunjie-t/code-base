package com.all.zeroth.hash;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.all.zeroth.interceptor.WebAppInterceptor;

@Configuration
@EnableAspectJAutoProxy
@Aspect
public class HashAopConfiguration {
	@Pointcut("execution(public * com.all.zeroth.hash.*.*(..))")
	public void monitoringHashOperations() {}
	
	@Bean
	public WebAppInterceptor hashServiceInterceptor() {
		return new WebAppInterceptor();
	}
	
	@Bean
	public Advisor hashServiceAdvisor() {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("com.all.zeroth.hash.HashAopConfiguration.monitoringHashOperations()");
		return new DefaultPointcutAdvisor(pointcut, hashServiceInterceptor());
	}
}

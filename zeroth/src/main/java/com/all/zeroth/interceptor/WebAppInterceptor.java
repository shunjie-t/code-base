package com.all.zeroth.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class WebAppInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		try {			
			this.doBefore(invocation);
			Object result = this.proceed(invocation);
			this.doAfter(invocation);
			
			return result;
		} catch(Throwable t) {
			this.doException(t, invocation);
			
			throw t;
		}
	}
	
	private void doBefore(MethodInvocation invocation) {
		System.out.println("doBefore with method: " + invocation.getMethod().getName());
	}
	
	private void doAfter(MethodInvocation invocation) {
		System.out.println("doAfter with method: " + invocation.getMethod().getName());
	}
	
	private void doException(Throwable throwable, MethodInvocation invocation) {
		System.out.println("doException with thrown: " + throwable + " method: " + invocation.getMethod().getName());
	}
	
	private Object proceed(MethodInvocation invocation) throws Throwable {
		long start = System.currentTimeMillis();
		try {
			return invocation.proceed();
		} finally {
			System.out.println(System.currentTimeMillis() - start);
		}
	}
}

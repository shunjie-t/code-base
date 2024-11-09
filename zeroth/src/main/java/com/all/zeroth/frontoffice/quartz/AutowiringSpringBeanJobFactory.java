package com.all.zeroth.frontoffice.quartz;

//import org.quartz.Job;
//import org.quartz.SchedulerContext;
//import org.quartz.spi.TriggerFiredBundle;
//import org.springframework.beans.BeanWrapper;
//import org.springframework.beans.MutablePropertyValues;
//import org.springframework.beans.PropertyAccessorFactory;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationContextAware;
//import org.springframework.scheduling.quartz.SpringBeanJobFactory;

public final class AutowiringSpringBeanJobFactory {
//public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory implements ApplicationContextAware {
//	private ApplicationContext applicationContext;
//	private SchedulerContext schedulerContext;
//	
//	@Override
//	public void setApplicationContext(final ApplicationContext applicationContext) {
//		this.applicationContext = applicationContext;
//	}
//	
//	@Override
//	protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
//		Job job = this.applicationContext.getBean(bundle.getJobDetail().getJobClass());
//		BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(job);
//		MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
//		mutablePropertyValues.addPropertyValues(bundle.getJobDetail().getJobDataMap());
//		mutablePropertyValues.addPropertyValues(bundle.getTrigger().getJobDataMap());
//		if(this.schedulerContext != null) {
//			mutablePropertyValues.addPropertyValues(this.schedulerContext);
//		}
//		beanWrapper.setPropertyValues(mutablePropertyValues, true);
//		return job;
//	}
//	
//	@Override
//	public void setSchedulerContext(SchedulerContext schedulerContext) {
//		this.schedulerContext = schedulerContext;
//		super.setSchedulerContext(schedulerContext);
//	}
}

package com.all.zeroth.quartzFrontOffice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.sql.DataSource;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class SchedulerConfig {
	@Bean
	public JobFactory jobFactory(ApplicationContext context) {
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(context);
		return jobFactory;
	}
	
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory, QuartzProperties quartzProperties) {
		Properties properties = new Properties();
		properties.putAll(quartzProperties.getProperties());
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setOverwriteExistingJobs(true);
		factory.setDataSource(dataSource);
		factory.setQuartzProperties(properties);
		factory.setJobFactory(jobFactory);
		return factory;
	}
	
	@Bean
	public Scheduler scheduler(Map<String, JobDetail> jobMap, Set<? extends Trigger> triggers, SchedulerFactoryBean factory, ApplicationContext context) throws SchedulerException {
		Scheduler scheduler = factory.getScheduler();
		scheduler.setJobFactory(jobFactory(context));
		
		Map<JobDetail, Set<? extends Trigger>> triggerAndJobs = new HashMap<>();
		for(JobDetail jobDetail : jobMap.values()) {
			for(Trigger trigger : triggers) {
				if(scheduler.checkExists(jobDetail.getKey()) && trigger.getJobKey().equals(jobDetail.getKey())) {
					Set<Trigger> set = new HashSet<>();
					set.add(trigger);
					triggerAndJobs.put(jobDetail, set);
				}
			}
		}
		scheduler.scheduleJobs(triggerAndJobs, false);
		scheduler.start();
		System.out.println("Scheduler initialized.");
		
		return scheduler;
	}
}

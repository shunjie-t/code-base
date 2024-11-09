package com.all.zeroth.quartz.method2;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.all.zeroth.quartz.SampleJob;

import jakarta.annotation.PostConstruct;

public class QuartzschedulerConfigTwo {
//	@Autowired
//	private SchedulerFactoryBean schedulerFactoryBean;
	
	@PostConstruct
	public void scheduleJob() throws SchedulerException {
		SchedulerFactory schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();
		scheduler.start();
		
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("key2", "value2");
		
		JobDetail jobDetail = JobBuilder.newJob(SampleJob.class)
				.withIdentity("job-key2", "job-grp2")
				.setJobData(jobDataMap).storeDurably().build();
		
		Trigger trigger = TriggerBuilder.newTrigger()
				.forJob("job-key2", "job-grp2")
				.withIdentity("trigger-key2", "trigger-grp2")
				.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
				.build();
			
		scheduler.scheduleJob(jobDetail, trigger);
//		schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);
	}
}

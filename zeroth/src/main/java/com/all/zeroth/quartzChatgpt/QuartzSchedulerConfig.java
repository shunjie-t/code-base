package com.all.zeroth.quartzChatgpt;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.StringUtils;

@Configuration
public class QuartzSchedulerConfig {
	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	public void scheduleJob() throws SchedulerException {
		JobDetail jobDetail = JobBuilder.newJob(SampleJob.class)
				.withIdentity("sampleJob").storeDurably().build();
		
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("sampleJobTrigger")
				.forJob("sampleJob").withSchedule(CronScheduleBuilder.cronSchedule("10 * * * * ?"))
				.build();
		
		schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, trigger);
		
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("parameter1", "value1");
		
		JobDetail jobDetail2 = JobBuilder.newJob(SampleJob.class).withIdentity("sampleJobWithParams")
				.setJobData(jobDataMap).storeDurably().build();
		
		schedulerFactoryBean.getScheduler().scheduleJob(jobDetail2, trigger);
		
		schedulerFactoryBean.start();
		
		if(schedulerFactoryBean.isRunning()) {			
			System.out.println("scheduler started");
		}
	}
}

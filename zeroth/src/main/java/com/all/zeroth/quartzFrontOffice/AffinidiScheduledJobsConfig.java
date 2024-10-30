package com.all.zeroth.quartzFrontOffice;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AffinidiScheduledJobsConfig {
	@Value("${scheduled.overseasvacc.housekeep.fixRate.milliseconds}")
	private long overseasVacHouseKeepingInterval;
	
	@Bean
	public JobDetail overseasVacHouseKeepingJobDetails() {
		return JobBuilder.newJob(OverseasVaccHouseKeepingJob.class)
				.withIdentity(OverseasVaccHouseKeepingJob.class.getName())
				.storeDurably().build();
	}
	
	@Bean
	public Trigger overseasVacHouseKeepingTrigger(JobDetail overseasVacHouseKeepingJobDetails) {
		return TriggerBuilder.newTrigger().forJob(overseasVacHouseKeepingJobDetails)
				.withIdentity(overseasVacHouseKeepingJobDetails.getClass().getName())
				.withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInMilliseconds(overseasVacHouseKeepingInterval))
				.build();
	}
}
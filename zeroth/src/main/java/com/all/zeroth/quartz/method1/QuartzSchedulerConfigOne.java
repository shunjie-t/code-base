package com.all.zeroth.quartz.method1;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.all.zeroth.quartz.SampleJob;

@Configuration
public class QuartzSchedulerConfigOne {
	@Bean
	public JobDetail sampleJobDetail() {
		JobDataMap jobDataMap = new JobDataMap();
		jobDataMap.put("key1", "value1");
		
        return JobBuilder.newJob(SampleJob.class)
				.withIdentity("job-key1", "job-grp1")
				.setJobData(jobDataMap).storeDurably().build();
    }
	
	@Bean
    public Trigger sampleJobTrigger() {
        return TriggerBuilder.newTrigger()
				.forJob("job-key1", "job-grp1")
				.withIdentity("trigger-key1", "trigger-grp2")
				.withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
				.build();
    }
}

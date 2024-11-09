package com.all.zeroth.quartz;

import java.time.LocalDateTime;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.springframework.stereotype.Component;

public class SampleJob implements Job {
	@Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
		JobKey key = context.getJobDetail().getKey();
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		Trigger trigger = context.getTrigger();
		
        System.out.printf("[Executing batch job at %s] ", LocalDateTime.now());
        System.out.printf("[data key: %s, data value: %s] ", "parameter1", dataMap.get("key1"));
        System.out.printf("[Job identity, key: %s, group: %s] ", key.getName(), key.getGroup());
		System.out.printf("[Trigger identity, key: %s, group: %s]%n", trigger.getKey().getName(), trigger.getKey().getGroup());
    }
}

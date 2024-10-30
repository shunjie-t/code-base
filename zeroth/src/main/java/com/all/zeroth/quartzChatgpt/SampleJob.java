package com.all.zeroth.quartzChatgpt;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class SampleJob implements Job {
	@Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // Job logic here
        System.out.println("Executing batch job at " + System.currentTimeMillis());
    }
}

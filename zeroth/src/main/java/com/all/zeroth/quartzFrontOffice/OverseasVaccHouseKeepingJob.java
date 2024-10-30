package com.all.zeroth.quartzFrontOffice;

import java.time.LocalDateTime;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//import sg.ica.eac.affinidi.dao.IAffinidiVaccinationDAO;

@Component
@DisallowConcurrentExecution
public class OverseasVaccHouseKeepingJob implements Job {
	@Value("${scheduled.overseasvacc.housekeep.duration.days}")
	private int housekeepDurationInDays;
	
//	@Autowired
//	private IAffinidiVaccinationDAO dao;

	@Override
	@Transactional
	public void execute(JobExecutionContext context) throws JobExecutionException {
		LocalDateTime houseKeepTargetBefore = LocalDateTime.now().minusDays(housekeepDurationInDays);
		System.out.println("Delete records after " + houseKeepTargetBefore.toString());
	}

}

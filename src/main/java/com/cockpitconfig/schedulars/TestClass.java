package com.cockpitconfig.schedulars;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import java.util.Date;

public class TestClass implements Job {

	// use http://www.quartz-scheduler.org/docs/tutorial/TutorialLesson02.html to receive email address and email text
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
	//	System.out.println("Hello World Quartz Scheduler: " + new Date());
	}

}
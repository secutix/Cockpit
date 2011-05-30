package com.cockpitconfig.schedulars;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.impl.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.JobBuilder.*;
import static org.quartz.DateBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class CheckRulesScheduler {

	public CheckRulesScheduler() throws Exception{

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		sched.start();

		JobDetail job = newJob(RuleCheck.class)
							.withIdentity("Rules", "AssertionGroup")
							.storeDurably()
							.requestRecovery()
							.build();

		Trigger trg = newTrigger()
						.withIdentity("TriggerRule")
						//.withPriority(6)
						.withSchedule(simpleSchedule()
							.withIntervalInMinutes(1)
							.repeatForever())
						.endAt(dateOf(16,0,0))
						.build();

		sched.scheduleJob(job, trg);
    }

}

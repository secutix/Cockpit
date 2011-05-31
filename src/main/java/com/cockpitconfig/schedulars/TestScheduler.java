package com.cockpitconfig.schedulars;

//import java.util.Date;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.JobBuilder.*;
import static org.quartz.DateBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class TestScheduler {

	public TestScheduler()throws Exception{

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		sched.start();

		JobDetail job = newJob(TestClass.class)
							.withIdentity("myJob", "myGroup")
							.storeDurably()
							.requestRecovery()
							.usingJobData("someKey", "someValue")
							.build();

		Trigger trg = newTrigger()
						.withIdentity("myTrigger")
						.startAt(futureDate(10, IntervalUnit.SECOND))
						.withPriority(6)
						.forJob(job)
						.withSchedule(simpleSchedule()
							.withIntervalInHours(5)
							.repeatForever())
						.endAt(dateOf(22,0,0))
						.build();

		sched.scheduleJob(job, trg);
    }

  public static void main (String args[]) {
	  try {
		  //CommunicationEmail email = new CommunicationEmail();
		  //email.sendEmail("pulkit110@gmail.com", "Ye ho gaya");
	  } catch (Exception e) {

	  }
  }
}
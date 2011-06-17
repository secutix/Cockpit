package com.cockpitconfig.schedulars;

import java.util.ArrayList;

import org.apache.ibatis.session.SqlSessionFactory;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cockpitconfig.db.AssertionConditionDAO;
import com.cockpitconfig.db.AssertionGroupDAO;
import com.cockpitconfig.objects.AssertionCondition;
import com.cockpitconfig.objects.AssertionGroup;

public class RuleCheck implements Job {

	public RuleCheck() {

	}

	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		SqlSessionFactory sf = MyBatisSqlSessionFactory.getSqlSessionFactory();
		AssertionGroupDAO agDao = new AssertionGroupDAO(sf);
		ArrayList<AssertionGroup> existingRules = agDao.getAllrules();
		int noOfExistingRules = existingRules.size();

		for (int i = 0; i < noOfExistingRules; ++i) {
			AssertionConditionDAO acDao = new AssertionConditionDAO(sf);
			ArrayList<AssertionCondition> existingCriterias = acDao
					.getRuleRow(existingRules.get(i).getId());
			for (int j = 0; j < existingCriterias.size(); ++j) {
				Integer minVal = existingCriterias.get(j).getMinVal();
				Integer maxVal = existingCriterias.get(j).getMaxVal();
				Integer minDel = existingCriterias.get(j).getMinDelta();
				Integer maxDel = existingCriterias.get(j).getMaxDelta();
				int isAre = -1;
				int slope = -1;
				if (minVal != null || maxVal != null) {
					isAre = 1;
				} else {
					isAre = 2;
				}
				if (minVal == maxVal || minDel == maxDel) {
					slope = 2;
				} else if (minVal != null || minDel != null) {
					slope = 3;
				} else {
					slope = 1;
				}
				int timeframe = existingCriterias.get(j).getTimeFrameID();
				int notification = existingCriterias.get(j).getNotificationID();
				// existingCriterias.get(j).get
			}
		}
	}
}
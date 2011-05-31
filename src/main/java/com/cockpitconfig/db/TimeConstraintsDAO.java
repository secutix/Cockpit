package com.cockpitconfig.db;

import java.util.ArrayList;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.cockpitconfig.objects.TimeConstraints;

public class TimeConstraintsDAO {
	private SqlSessionFactory sf;
	//constructor will receive a myBatis sessionFactory object
	public TimeConstraintsDAO (SqlSessionFactory containerSessionFactory) {
		if(containerSessionFactory==null) {
			System.err.println("Error: could not load myBatis sessionFactory");
		}
		sf = containerSessionFactory;
	}

	public void setTimeConstraintForRule (TimeConstraints tcs) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			session.selectList("com.cockpitconfig.objects.CommunicationMapper.setRuleTime", tcs);
		} finally {
			session.close();
		}
	}

	public void removeTimeConstraintsWithID (int grpID) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			session.selectList("com.cockpitconfig.objects.CommunicationMapper.removeTimeConstraintsWithGivenID", grpID);
		} finally {
			session.close();
		}
	}

	public ArrayList<TimeConstraints> getfrequencyRow(int grpID) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			ArrayList<TimeConstraints> frequencyRow = (ArrayList<TimeConstraints>)session.selectList("com.cockpitconfig.objects.CommunicationMapper.getFrequencyInfo", grpID);
			if (frequencyRow == null) {
				throw new PersistenceException();		//TODO: Do Better Error handling
			}
			return frequencyRow;
		} finally {
			session.close();
		}
	}
}
package com.cockpitconfig.db;

import java.util.ArrayList;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.cockpitconfig.objects.TimeConstraints;

public class TimeConstraintsDAO {
	private SqlSessionFactory sf;

	// constructor will receive a myBatis sessionFactory object
	public TimeConstraintsDAO(SqlSessionFactory containerSessionFactory) {
		if (containerSessionFactory == null) {
			System.err.println("Error: could not load myBatis sessionFactory");
		}
		sf = containerSessionFactory;
	}

	/**
	 * Function to set the time constraints for the rule
	 * 
	 * @param tcs
	 *            obj holding the information
	 * @throws PersistenceException
	 */
	public void setTimeConstraintForRule(TimeConstraints tcs) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			session.insert("com.cockpitconfig.objects.CommunicationMapper.setRuleTime", tcs);
		} finally {
			session.commit();
			session.close();
		}
	}

	public void removeTimeConstraintsWithID(int grpID) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			session.selectList("com.cockpitconfig.objects.CommunicationMapper.removeTimeConstraintsWithGivenID", grpID);
		} finally {
			session.close();
		}
	}

	/**
	 * Function to get a row from TimeConstraints table for a given id
	 * 
	 * @param grpID
	 * @return
	 * @throws PersistenceException
	 */
	public ArrayList<TimeConstraints> getfrequencyRow(int grpID) throws PersistenceException {
		ArrayList<TimeConstraints> frequencyRow = null;
		SqlSession session = sf.openSession();
		try {
			frequencyRow = (ArrayList<TimeConstraints>) session.selectList("com.cockpitconfig.objects.CommunicationMapper.getFrequencyInfo", grpID);
			if (frequencyRow == null) {
				throw new PersistenceException(); // TODO: Do Better Error handling
			}
		} finally {
			session.close();
		}

		return frequencyRow;
	}
}

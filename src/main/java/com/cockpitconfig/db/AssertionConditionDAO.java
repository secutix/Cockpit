package com.cockpitconfig.db;

import java.util.ArrayList;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.cockpitconfig.objects.AssertionCondition;

public class AssertionConditionDAO {
	private SqlSessionFactory sf;

	// constructor will receive a myBatis sessionFactory object
	public AssertionConditionDAO(SqlSessionFactory containerSessionFactory) {
		if (containerSessionFactory == null) {
			System.err.println("Error: could not load myBatis sessionFactory");
		}

		sf = containerSessionFactory;
	}

	/**
	 * Function to insert a new rule whenever user adds a new rule
	 *
	 * @param tempobj
	 *            Object to hold the information
	 * @throws PersistenceException
	 */
	public void addNewRule(AssertionCondition tempobj) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			session.insert("com.cockpitconfig.objects.CommunicationMapper.addRule", tempobj);
		} finally {
			session.commit();
			session.close();
		}
	}

	/**
	 * Function to get the row corresponding to given PK
	 *
	 * @param grpID
	 *            PK of the rule
	 * @return Row containing the information regarding the rule
	 * @throws PersistenceException
	 */
	public ArrayList<AssertionCondition> getRuleRow(int grpID) throws PersistenceException {
		ArrayList<AssertionCondition> ruleRow = null;
		SqlSession session = sf.openSession();
		try {
			ruleRow = (ArrayList<AssertionCondition>) session.selectList("com.cockpitconfig.objects.CommunicationMapper.getRuleInfo", grpID);
			if (ruleRow == null) {
				throw new PersistenceException(); // TODO: Do Better Error handling
			}
		} finally {
			session.close();
		}

		return ruleRow;
	}

	/**
	 * Removes the rule from the assertionCondition table with the given PK
	 *
	 * @param grpID
	 *            PK of the rule
	 * @throws PersistenceException
	 */
	public void removeRulesWithID(int grpID) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			session.selectList("com.cockpitconfig.objects.CommunicationMapper.deleteRulesWithID", grpID);
		} finally {
			session.close();
		}
	}
}

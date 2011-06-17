package com.cockpitconfig.db;

import java.util.ArrayList;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.cockpitconfig.objects.AssertionGroup;

public class AssertionGroupDAO {
	private SqlSessionFactory sf;

	// constructor will receive a myBatis sessionFactory object
	public AssertionGroupDAO(SqlSessionFactory containerSessionFactory) {
		if (containerSessionFactory == null) {
			System.err.println("Error: could not load myBatis sessionFactory");
		}

		sf = containerSessionFactory;
	}

	public void setConstraintName(AssertionGroup assGrp)
			throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			session.selectList(
					"com.cockpitconfig.objects.CommunicationMapper.setRuleName",
					assGrp);
		} finally {
			session.close();
		}
	}

	public int getPKForRule(String ruleName) throws PersistenceException {
		ArrayList<AssertionGroup> getRuleRow = null;
		SqlSession session = sf.openSession();
		try {
			getRuleRow = (ArrayList<AssertionGroup>) session
					.selectList(
							"com.cockpitconfig.objects.CommunicationMapper.getPKForConstraint",
							ruleName);
			if (getRuleRow == null || getRuleRow.size() == 0) {
				return -1;
			} else {
				return getRuleRow.get(0).getId();
			}
		} finally {
			session.close();
		}
	}

	public ArrayList<AssertionGroup> getGrpRow(int grpID)
			throws PersistenceException {
		ArrayList<AssertionGroup> getGrpRow = null;
		SqlSession session = sf.openSession();
		try {
			getGrpRow = (ArrayList<AssertionGroup>) session
					.selectList(
							"com.cockpitconfig.objects.CommunicationMapper.getGroupRow",
							grpID);
			if (getGrpRow == null || getGrpRow.size() == 0) {
				return null;
			} else {
				return getGrpRow;
			}
		} finally {
			session.close();
		}
	}

	public void updateCommMedium(AssertionGroup ag) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			session.selectList(
					"com.cockpitconfig.objects.CommunicationMapper.updateCommID",
					ag);
		} finally {
			session.close();
		}
	}

	public int getLastInsertedIndex(AssertionGroup assGrp)
			throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			ArrayList<AssertionGroup> lastInsertedRow = (ArrayList<AssertionGroup>) session
					.selectList("com.cockpitconfig.objects.CommunicationMapper.getLastInsertedID");
			int lastInsertedID = lastInsertedRow.get(0).getId();
			return lastInsertedID;
		} finally {
			session.close();
		}
	}

	public ArrayList<AssertionGroup> getAllrules() throws PersistenceException {
		ArrayList<AssertionGroup> rules = null;
		SqlSession session = sf.openSession();
		try {
			rules = (ArrayList<AssertionGroup>) session
					.selectList("com.cockpitconfig.objects.CommunicationMapper.getAllRuleName");
			if (rules == null) {
				throw new PersistenceException();
			}
		} finally {
			session.close();
		}

		return rules;
	}

	/**
	 * Function which retrieves Constraint Name for printing Alert Message when
	 * user tries to delete a url from ManageSource Screen which can not be
	 * deleted
	 * 
	 * @param pkForSource
	 * @return
	 * @throws PersistenceException
	 */
	public String checkForSource(int pkForSource) throws PersistenceException {
		ArrayList<AssertionGroup> checkExists = null;
		SqlSession session = sf.openSession();
		try {
			checkExists = (ArrayList<AssertionGroup>) session
					.selectList(
							"com.cockpitconfig.objects.CommunicationMapper.checkForGivenSource",
							pkForSource);
			if (checkExists == null) {
				return null;
			} else {
				return checkExists.get(0).getConstraintName();
			}
		} finally {
			session.close();
		}
	}

	/**
	 * Function which return PK for a given Constraint Name
	 * 
	 * @param ruleName
	 * @return
	 * @throws PersistenceException
	 */
	public int getSourceKeyForGivenRule(String ruleName)
			throws PersistenceException {
		Integer source = null;
		SqlSession session = sf.openSession();
		try {
			source = (Integer) session
					.selectOne(
							"com.cockpitconfig.objects.CommunicationMapper.getSourceKeyForGivenConstraint",
							ruleName);
			if (source == null) {
				throw new PersistenceException();
			}
		} finally {
			session.close();
		}
		return source.intValue();
	}

}
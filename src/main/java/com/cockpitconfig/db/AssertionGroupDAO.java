package com.cockpitconfig.db;

import java.util.ArrayList;
import java.util.HashMap;

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

	/**
	 * Function to set the constraint name, communication medium and source in
	 * the assertionGroup table
	 * 
	 * @param assGrp
	 *            Object of assertionGroup class holding the information
	 * @throws PersistenceException
	 */
	public void setConstraintName(AssertionGroup assGrp)
			throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			session.insert(
					"com.cockpitconfig.objects.CommunicationMapper.setRuleName",
					assGrp);
		} finally {
			session.commit();
			session.close();
		}
	}

	/**
	 * Function to get the PK for a given rule, invoked when user selects an
	 * existing rule
	 * 
	 * @param ruleName
	 *            Rule selected by user
	 * @return returns the PK from the assertionGroup table for given rule
	 * @throws PersistenceException
	 */
	public int getPKForRule(String ruleName) throws PersistenceException {
		Integer PK;
		SqlSession session = sf.openSession();
		try {
			PK = (Integer) session
					.selectOne(
							"com.cockpitconfig.objects.CommunicationMapper.getPKForConstraint",
							ruleName);
			if (PK == null) {
				return -1;
			}
		} finally {
			session.close();
		}

		return PK.intValue();
	}

	/**
	 * @param grpID
	 * @return
	 * @throws PersistenceException
	 */
	public ArrayList<AssertionGroup> getGrpRow(int grpID)
			throws PersistenceException {
		ArrayList<AssertionGroup> groupRow = null;
		SqlSession session = sf.openSession();
		try {
			groupRow = (ArrayList<AssertionGroup>) session
					.selectList(
							"com.cockpitconfig.objects.CommunicationMapper.getGroupRow",
							grpID);
			if (groupRow == null || groupRow.size() == 0) {
				return null;
			}
		} finally {
			session.close();
		}

		return groupRow;
	}

	/**
	 * Update communication Medium
	 * 
	 * @param ag
	 * @throws PersistenceException
	 */
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

	/**
	 * Function to get the PK for last inserted rule
	 * 
	 * @param assGrp
	 *            temp obj to hold the information
	 * @return PK for the last inserted rule
	 * @throws PersistenceException
	 */
	public int getLastInsertedIndex() throws PersistenceException {
		Integer lastInserted;
		SqlSession session = sf.openSession();
		try {
			lastInserted = (Integer) session
					.selectOne("com.cockpitconfig.objects.CommunicationMapper.getLastInsertedID");
			if (lastInserted == null) {
				throw new PersistenceException();
			}
		} finally {
			session.close();
		}

		return lastInserted.intValue();
	}

	/**
	 * Function to get the all rows of the assertionGroup table
	 * 
	 * @return all rows of the assertionGroup table
	 * @throws PersistenceException
	 */
	public ArrayList<AssertionGroup> getAllrules() throws PersistenceException {
		ArrayList<AssertionGroup> rules = null;
		SqlSession session = sf.openSession();
		try {
			rules = (ArrayList<AssertionGroup>) session
					.selectList("com.cockpitconfig.objects.CommunicationMapper.getAllRules");
			if (rules == null) {
				throw new PersistenceException();
			}
		} finally {
			session.close();
		}

		return rules;
	}

	/**
	 * Function to get the all ruleName of the assertionGroup table
	 * 
	 * @return all ruleName of the assertionGroup table
	 * @throws PersistenceException
	 */
	public ArrayList<String> getRuleNames() throws PersistenceException {
		ArrayList<String> ruleNames = null;
		SqlSession session = sf.openSession();
		try {
			ruleNames = (ArrayList<String>) session
					.selectList("com.cockpitconfig.objects.CommunicationMapper.getAllRuleNames");
			if (ruleNames == null) {
				throw new PersistenceException();
			}
		} finally {
			session.close();
		}

		return ruleNames;
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
		String checkExists;
		SqlSession session = sf.openSession();
		try {
			checkExists = (String) session
					.selectOne(
							"com.cockpitconfig.objects.CommunicationMapper.checkForGivenSource",
							pkForSource);
			if (checkExists == null) {
				return null;
			} else {
				return checkExists;
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

	/**
	 * Not used
	 * 
	 * @return
	 * @throws PersistenceException
	 */
	public int getTotalNumberOfRules() throws PersistenceException {
		Integer source = null;
		SqlSession session = sf.openSession();
		try {
			source = (Integer) session
					.selectOne("com.cockpitconfig.objects.CommunicationMapper.getTotalCountofRules");
			if (source == null) {
				throw new PersistenceException();
			}
		} finally {
			session.close();
		}
		return source.intValue();
	}

	/**
	 * Return list of integer containing PK for all rules
	 * 
	 * @return
	 * @throws PersistenceException
	 */
	public HashMap getAllPKandSource() throws PersistenceException {
		HashMap hMap = new HashMap();
		SqlSession session = sf.openSession();
		try {
			hMap = (HashMap) session
					.selectMap(
							"com.cockpitconfig.objects.CommunicationMapper.getAllPKAndSources",
							"id");
			if (hMap == null) {
				throw new PersistenceException();
			}
		} finally {
			session.close();
		}
		return hMap;
	}

	public void removeRule(String ruleName) {
		SqlSession session = sf.openSession();
		try {
			session.delete(
					"com.cockpitconfig.objects.CommunicationMapper.removeGivenRule",
					ruleName);
		} finally {
			session.commit();
			session.close();
		}
	}
}
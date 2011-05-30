package com.cockpitconfig.db;

import java.util.ArrayList;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.cockpitconfig.objects.*;

public class AssertionGroupDAO {
	private SqlSessionFactory sf;

	//constructor will receive a myBatis sessionFactory object
	public AssertionGroupDAO (SqlSessionFactory containerSessionFactory) {
		if(containerSessionFactory==null) {
			System.err.println("Error: could not load myBatis sessionFactory");
		}

		sf = containerSessionFactory;
	}

	public void setConstraintName (AssertionGroup assGrp) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			session.selectList("com.cockpitconfig.objects.CommunicationMapper.setRuleName", assGrp);
		} finally {
			session.close();
		}
	}

	public int getPKForRule (String ruleName) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			ArrayList<AssertionGroup> getRuleRow = (ArrayList<AssertionGroup>)session.selectList("com.cockpitconfig.objects.CommunicationMapper.getPKForConstraint", ruleName);
			if(getRuleRow == null || getRuleRow.size() == 0) {
				return -1;
			} else {
				return getRuleRow.get(0).getId();
			}
		} finally {
			session.close();
		}
	}

	public ArrayList<AssertionGroup> getGrpRow (int grpID) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			ArrayList<AssertionGroup> getGrpRow = (ArrayList<AssertionGroup>)session.selectList("com.cockpitconfig.objects.CommunicationMapper.getGroupRow", grpID);
			if(getGrpRow == null || getGrpRow.size() == 0) {
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
			session.selectList("com.cockpitconfig.objects.CommunicationMapper.updateCommID", ag);
		} finally {
			session.close();
		}
	}

	public int getLastInsertedIndex (AssertionGroup assGrp) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			ArrayList<AssertionGroup> lastInsertedRow = (ArrayList<AssertionGroup>)session.selectList("com.cockpitconfig.objects.CommunicationMapper.getLastInsertedID");
			int lastInsertedID = lastInsertedRow.get(0).getId();
			return lastInsertedID;
		} finally {
			session.close();
		}
	}

	public ArrayList<AssertionGroup> getAllrules() throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			ArrayList<AssertionGroup> rules = (ArrayList<AssertionGroup>)session.selectList("com.cockpitconfig.objects.CommunicationMapper.getAllRuleName");

			if (rules == null) {
				throw new PersistenceException();		//TODO: Do Better Error handling
			}
			return rules;
		} finally {
			session.close();
		}
	}

}
package com.cockpitconfig.db;

import java.util.ArrayList;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.cockpitconfig.objects.AssertionCondition;

public class AssertionConditionDAO {
	private SqlSessionFactory sf;

	//constructor will receive a myBatis sessionFactory object
	public AssertionConditionDAO (SqlSessionFactory containerSessionFactory) {
		if(containerSessionFactory==null) {
			System.err.println("Error: could not load myBatis sessionFactory");
		}

		sf = containerSessionFactory;
	}

	public void addNewRule (AssertionCondition tempobj) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			session.selectList("com.cockpitconfig.objects.CommunicationMapper.addRule", tempobj);
		} finally {
			session.close();
		}
	}

	public ArrayList<AssertionCondition> getRuleRow(int grpID) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			ArrayList<AssertionCondition> ruleRow = (ArrayList<AssertionCondition>)session.selectList("com.cockpitconfig.objects.CommunicationMapper.getRuleInfo", grpID);
			//assertNotNull("Medium list is null",comm);
			if (ruleRow == null) {
				throw new PersistenceException();		//TODO: Do Better Error handling
			}
			return ruleRow;
		} finally {
			session.close();
		}
	}

	public void removeRulesWithID (int grpID) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			session.selectList("com.cockpitconfig.objects.CommunicationMapper.deleteRulesWithID", grpID);
		} finally {
			session.close();
		}
	}
}
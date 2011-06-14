package com.cockpitconfig.db;

import java.util.ArrayList;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.cockpitconfig.objects.Criteria;

public class CriteriaDAO {
	private SqlSessionFactory sf;
	//constructor will receive a myBatis sessionFactory object
	public CriteriaDAO (SqlSessionFactory containerSessionFactory) {
		if(containerSessionFactory==null) {
			System.err.println("Error: could not load myBatis sessionFactory");
		}
		sf = containerSessionFactory;
	}

	public ArrayList<Criteria> getAllCriterias() throws PersistenceException {
		ArrayList<Criteria> conditions = null;
		SqlSession session = sf.openSession();
		try {
			conditions = (ArrayList<Criteria>)session.selectList("com.cockpitconfig.objects.CommunicationMapper.getAllConditions");
			if (conditions == null) {
				throw new PersistenceException();		//TODO: Do Better Error handling
			}
		}
		finally {
			session.close();
		}

		return conditions;
	}

	public ArrayList<Criteria> getCriteriaByID(int getID) throws PersistenceException {
		ArrayList<Criteria> conditions = null;
		SqlSession session = sf.openSession();
		try {
			conditions = (ArrayList<Criteria>)session.selectList("com.cockpitconfig.objects.CommunicationMapper.getConditionByID", getID);
			if (conditions == null) {
				throw new PersistenceException();		//TODO: Do Better Error handling
			}
		} finally {
			session.close();
		}

		return conditions;
	}
}
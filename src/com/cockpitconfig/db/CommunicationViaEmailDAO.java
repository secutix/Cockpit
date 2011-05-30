package com.cockpitconfig.db;

import java.util.ArrayList;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.cockpitconfig.objects.CommunicationViaEmail;;

public class CommunicationViaEmailDAO {
	private SqlSessionFactory sf;

	//constructor will receive a myBatis sessionFactory object
	public CommunicationViaEmailDAO (SqlSessionFactory containerSessionFactory) {
		if(containerSessionFactory==null) {
			System.err.println("Error: could not load myBatis sessionFactory");
		}

		sf = containerSessionFactory;
	}

	public ArrayList<CommunicationViaEmail> getEmailRow (int grpID) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			ArrayList<CommunicationViaEmail> recipents = (ArrayList<CommunicationViaEmail>) session.selectList("com.cockpitconfig.objects.CommunicationMapper.getEmailRowInfo", grpID);
			if (recipents == null) {
				throw new PersistenceException();		//TODO: Do Better Error handling
			}

			return recipents;
		} finally {
			session.close();
		}
	}

	public void removeEmailRecipentWithID (int grpID) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			session.selectList("com.cockpitconfig.objects.CommunicationMapper.removeEmailRecipentWithGivenID", grpID);
		} finally {
			session.close();
		}
	}

	public void addEmailRecipents (CommunicationViaEmail viaEmail) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			session.selectList("com.cockpitconfig.objects.CommunicationMapper.addRecipents", viaEmail);
		} finally {
			session.close();
		}
	}
}
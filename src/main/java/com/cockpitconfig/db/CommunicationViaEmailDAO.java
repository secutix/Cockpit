package com.cockpitconfig.db;

import java.util.ArrayList;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.cockpitconfig.objects.CommunicationViaEmail;

public class CommunicationViaEmailDAO {
	private SqlSessionFactory sf;

	// constructor will receive a myBatis sessionFactory object
	public CommunicationViaEmailDAO(SqlSessionFactory containerSessionFactory) {
		if (containerSessionFactory == null) {
			System.err.println("Error: could not load myBatis sessionFactory");
		}

		sf = containerSessionFactory;
	}

	/**
	 * Get the communication data for a given ruleName
	 * 
	 * @param grpID
	 *            PK of the rule
	 * @return
	 * @throws PersistenceException
	 */
	public ArrayList<CommunicationViaEmail> getEmailRow(int grpID)
			throws PersistenceException {
		ArrayList<CommunicationViaEmail> recipents = null;
		SqlSession session = sf.openSession();
		try {
			recipents = (ArrayList<CommunicationViaEmail>) session
					.selectList(
							"com.cockpitconfig.objects.CommunicationMapper.getEmailRowInfo",
							grpID);
			if (recipents == null) {
				throw new PersistenceException(); // TODO: Do Better Error
													// handling
			}
		} finally {
			session.close();
		}

		return recipents;
	}

	/**
	 * Remove the communication medium for the given id
	 * 
	 * @param grpID
	 *            PK of the rule
	 * @throws PersistenceException
	 */
	public void removeEmailRecipentWithID(int grpID)
			throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			session.selectList(
					"com.cockpitconfig.objects.CommunicationMapper.removeEmailRecipentWithGivenID",
					grpID);
		} finally {
			session.close();
		}
	}

	/**
	 * Adds email recipient in the table
	 * 
	 * @param viaEmail
	 * @throws PersistenceException
	 */
	public void addEmailRecipients(CommunicationViaEmail viaEmail)
			throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			session.insert(
					"com.cockpitconfig.objects.CommunicationMapper.addRecipients",
					viaEmail);
		} finally {
			session.commit();
			session.close();
		}
	}

	public String getrecipient(int grpID) throws PersistenceException {
		String recipient;
		SqlSession session = sf.openSession();
		try {
			recipient = (String) session
					.selectOne(
							"com.cockpitconfig.objects.CommunicationMapper.getEmailRecipient",
							grpID);
			if (recipient == null) {
				throw new PersistenceException();
			}
		} finally {
			session.close();
		}

		return recipient;
	}
}

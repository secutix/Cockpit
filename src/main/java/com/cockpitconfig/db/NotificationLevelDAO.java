package com.cockpitconfig.db;

import java.util.ArrayList;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.cockpitconfig.objects.NotificationLevel;

public class NotificationLevelDAO {
	private SqlSessionFactory sf;

	// constructor will receive a myBatis sessionFactory object
	public NotificationLevelDAO(SqlSessionFactory containerSessionFactory) {

		if (containerSessionFactory == null) {
			System.err.println("Error: could not load myBatis sessionFactory");
		}
		sf = containerSessionFactory;
	}

	/**
	 * Function to get all the notification types from the notificationLevel table
	 * 
	 * @return list of all notification types
	 * @throws PersistenceException
	 */
	public ArrayList<NotificationLevel> getAllNotificationLevels() throws PersistenceException {
		ArrayList<NotificationLevel> levels = null;
		SqlSession session = sf.openSession();
		try {
			levels = (ArrayList<NotificationLevel>) session.selectList("com.cockpitconfig.objects.CommunicationMapper.getAllLevels");
			if (levels == null) {
				throw new PersistenceException(); // TODO: Do Better Error handling
			}
		} finally {
			session.close();
		}

		return levels;
	}

	/**
	 * Function to get the notification for a given id
	 * 
	 * @param getID
	 *            given id
	 * @return notification type
	 * @throws PersistenceException
	 */
	public ArrayList<NotificationLevel> getNotificationLevelByID(int getID) throws PersistenceException {
		ArrayList<NotificationLevel> levels = null;
		SqlSession session = sf.openSession();
		try {
			levels = (ArrayList<NotificationLevel>) session.selectList("com.cockpitconfig.objects.CommunicationMapper.getLevelByID", getID);
			if (levels == null) {
				throw new PersistenceException(); // TODO: Do Better Error handling
			}
		} finally {
			session.close();
		}

		return levels;
	}
}

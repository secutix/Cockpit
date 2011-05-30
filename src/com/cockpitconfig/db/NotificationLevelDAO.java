package com.cockpitconfig.db;

import java.util.ArrayList;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.cockpitconfig.objects.NotificationLevel;

public class NotificationLevelDAO {
	private SqlSessionFactory sf;
	//constructor will receive a myBatis sessionFactory object
	public NotificationLevelDAO (SqlSessionFactory containerSessionFactory) {

		if(containerSessionFactory==null) {
			System.err.println("Error: could not load myBatis sessionFactory");
		}
		sf = containerSessionFactory;
	}

	public ArrayList<NotificationLevel> getAllNotificationLevels() throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			ArrayList<NotificationLevel> levels = (ArrayList<NotificationLevel>)session.selectList("com.cockpitconfig.objects.CommunicationMapper.getAllLevels");
			if (levels == null) {
				throw new PersistenceException();		//TODO: Do Better Error handling
			}

			return levels;
		} finally {
			session.close();
		}

	}

	public ArrayList<NotificationLevel> getNotificationLevelByID(int getID) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			ArrayList<NotificationLevel> levels = (ArrayList<NotificationLevel>)session.selectList("com.cockpitconfig.objects.CommunicationMapper.getLevelByID", getID);
			if (levels == null) {
				throw new PersistenceException();		//TODO: Do Better Error handling
			}

			return levels;
		} finally {
			session.close();
		}
	}
}
package com.cockpitconfig.db;

import java.util.ArrayList;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.cockpitconfig.objects.NotificationOccurrence;

import java.util.HashMap;

public class NotificationOccurrencesDAO {
	private SqlSessionFactory sf;
	//constructor will receive a myBatis sessionFactory object
	public NotificationOccurrencesDAO (SqlSessionFactory containerSessionFactory) {

		if(containerSessionFactory==null) {
			System.err.println("Error: could not load myBatis sessionFactory");
		}
		sf = containerSessionFactory;
	}

	public ArrayList<NotificationOccurrence> getAlerts(HashMap tempHashMap) throws PersistenceException {
		ArrayList<NotificationOccurrence> alerts = null;
		SqlSession session = sf.openSession();
		try {
			alerts = (ArrayList<NotificationOccurrence>)session.selectList("com.cockpitconfig.objects.CommunicationMapper.getNotifications", tempHashMap);
			if (alerts == null) {
				throw new PersistenceException();		//TODO: Do Better Error handling
			}
		} finally {
			session.close();
		}
		return alerts;
	}

	public int getTotalCount() throws PersistenceException {
		Integer a = null;
		SqlSession session = sf.openSession();
		try {
			a = (Integer) session.selectOne("com.cockpitconfig.objects.CommunicationMapper.getNotificationsCount");
			if (a == null) {
				throw new PersistenceException();		//TODO: Do Better Error handling
			}
		} finally {
			session.close();
		}
		return a;
	}
}
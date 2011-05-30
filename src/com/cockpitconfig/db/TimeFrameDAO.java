package com.cockpitconfig.db;

import java.util.ArrayList;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;


import com.cockpitconfig.objects.TimeFrame;

public class TimeFrameDAO {

	private SqlSessionFactory sf;
	//constructor will receive a myBatis sessionFactory object
	public TimeFrameDAO (SqlSessionFactory containerSessionFactory) {
		if(containerSessionFactory==null) {
			System.err.println("Error: could not load myBatis sessionFactory");
		}

		sf = containerSessionFactory;
	}

	public ArrayList<TimeFrame> getAllTimeFrames() throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			ArrayList<TimeFrame> frames = (ArrayList<TimeFrame>)session.selectList("com.cockpitconfig.objects.CommunicationMapper.getAllFrames");
			//assertNotNull("Medium list is null",comm);
			if (frames == null) {
				throw new PersistenceException();		//TODO: Do Better Error handling
			}			return frames;
		} finally {
			session.close();
		}
	}

	public ArrayList<TimeFrame> getTimeFrameByID(int getID) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			ArrayList<TimeFrame> frames = (ArrayList<TimeFrame>)session.selectList("com.cockpitconfig.objects.CommunicationMapper.getFrameByID", getID);
			if (frames == null) {
				throw new PersistenceException();		//TODO: Do Better Error handling
			}

			return frames;
		} finally {
			session.close();
		}
	}

}
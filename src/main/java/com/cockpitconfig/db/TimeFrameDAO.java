package com.cockpitconfig.db;

import java.util.ArrayList;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.cockpitconfig.objects.TimeFrame;

public class TimeFrameDAO {

	private SqlSessionFactory sf;

	// constructor will receive a myBatis sessionFactory object
	public TimeFrameDAO(SqlSessionFactory containerSessionFactory) {
		if (containerSessionFactory == null) {
			System.err.println("Error: could not load myBatis sessionFactory");
		}

		sf = containerSessionFactory;
	}

	/**
	 * Function to get all the timeFrames from the ImeFrame table
	 * 
	 * @return list of all notification types
	 * @throws PersistenceException
	 */
	public ArrayList<TimeFrame> getAllTimeFrames() throws PersistenceException {
		ArrayList<TimeFrame> frames = null;
		SqlSession session = sf.openSession();
		try {
			frames = (ArrayList<TimeFrame>) session.selectList("com.cockpitconfig.objects.CommunicationMapper.getAllFrames");
			if (frames == null) {
				throw new PersistenceException(); // TODO: Do Better Error handling
			}
		} finally {
			session.close();
		}
		return frames;
	}

	/**
	 * Function to get the timeFrame for a given id
	 * 
	 * @param getID
	 *            given id
	 * @return timeFrame
	 * @throws PersistenceException
	 */
	public ArrayList<TimeFrame> getTimeFrameByID(int getID) throws PersistenceException {
		ArrayList<TimeFrame> frames = null;
		SqlSession session = sf.openSession();
		try {
			frames = (ArrayList<TimeFrame>) session.selectList("com.cockpitconfig.objects.CommunicationMapper.getFrameByID", getID);
			if (frames == null) {
				throw new PersistenceException(); // TODO: Do Better Error handling
			}
		} finally {
			session.close();
		}
		return frames;
	}
}

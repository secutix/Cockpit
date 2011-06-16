package com.cockpitconfig.db;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.w3c.dom.ls.LSInput;

import com.cockpitconfig.objects.Sources;

public class SourcesDAO {

	private SqlSessionFactory sf;
	//constructor will receive a myBatis sessionFactory object
	public SourcesDAO (SqlSessionFactory containerSessionFactory) {
		if(containerSessionFactory==null) {
			System.err.println("Error: could not load myBatis sessionFactory");
		}
		sf = containerSessionFactory;
	}

	/**
	 * Function which retrieves sources from DB to display them on ManageSource Screen, no need to load all sources
	 * @param tempHashMap Specifies how many sources to load from DB to display
	 * @return List of sourcs according to tempHashMap
	 * @throws PersistenceException
	 */
	public ArrayList<Sources> getAllSources (HashMap tempHashMap) throws PersistenceException {
		ArrayList<Sources> src = null;
		SqlSession session = sf.openSession();
		try {
			src = (ArrayList<Sources>) session.selectList("com.cockpitconfig.objects.CommunicationMapper.getSources", tempHashMap);
			if (src == null) {
				throw new PersistenceException();		//TODO: Do Better Error handling
			}
		} finally {
			session.close();
		}

		return src;
	}

	/**
	 * Function which retrieves all sources from DB to show show them on AssertionScreen
	 * @param tempHashMap
	 * @return
	 * @throws PersistenceException
	 */
	public ArrayList<Sources> getTotalSources () throws PersistenceException {
		ArrayList<Sources> src = null;
		SqlSession session = sf.openSession();
		try {
			src = (ArrayList<Sources>) session.selectList("com.cockpitconfig.objects.CommunicationMapper.getCompleteSourceList");
			if (src == null) {
				throw new PersistenceException();		//TODO: Do Better Error handling
			}
		} finally {
			session.close();
		}

		return src;
	}

	public int getTotalSourceCount() throws PersistenceException {
		Integer totalNumberOfSources = null;
		SqlSession session = sf.openSession();
		try {
			totalNumberOfSources = (Integer) session.selectOne("com.cockpitconfig.objects.CommunicationMapper.getSourceCount");
			if (totalNumberOfSources== null) {
				throw new PersistenceException();		//TODO: Do Better Error handling
			}
		} finally {
			session.close();
		}

		return totalNumberOfSources;
	}

	public void removeSelectedSource(String selectedUrl) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			session.selectOne("com.cockpitconfig.objects.CommunicationMapper.removeSource", selectedUrl);
		} finally {
			session.close();
		}
	}

	public void addSource(Sources source) throws PersistenceException {
		SqlSession session = sf.openSession();
		try {
			session.insert("com.cockpitconfig.objects.CommunicationMapper.addNewSource", source);
		} finally {
			session.commit();
			session.close();
		}
	}

	public int getPKForSource(String source) throws PersistenceException {
		ArrayList<Sources> src = null;
		SqlSession session = sf.openSession();
		try {
			src =  (ArrayList<Sources>) session.selectList("com.cockpitconfig.objects.CommunicationMapper.getPKForGivenSource", source);
			if (src == null) {
				throw new PersistenceException();		//TODO: Do Better Error handling
			}
		} finally {
			session.close();
		}
		return src.get(0).getId();
	}
}
package com.cockpitconfig.db;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

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
}
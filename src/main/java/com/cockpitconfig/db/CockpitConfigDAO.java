package com.cockpitconfig.db;

import java.util.ArrayList;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;


import com.cockpitconfig.objects.Communication;

public class CockpitConfigDAO {
	private SqlSessionFactory sf;

	//constructor will receive a myBatis sessionFactory object
	public CockpitConfigDAO (SqlSessionFactory containerSessionFactory) {
		if(containerSessionFactory==null) {
			System.err.println("Error: could not load myBatis sessionFactory");
		}

		sf = containerSessionFactory;
	}

	public ArrayList<Communication> getClients() throws PersistenceException {
		SqlSession session = sf.openSession();
		try
		{
			ArrayList<Communication> comm = (ArrayList<Communication>)session.selectList("com.cockpitconfig.objects.CommunicationMapper.getMediumByID", 1);
			if (comm == null) {
				throw new PersistenceException();		//TODO: Do Better Error handling
			}
			return comm;
		} finally
		{
			session.close();
		}

	}
}
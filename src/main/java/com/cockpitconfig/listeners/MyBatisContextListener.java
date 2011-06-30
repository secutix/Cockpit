package com.cockpitconfig.listeners;

import java.io.Reader;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisContextListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent event) {

		ServletContext ctx = event.getServletContext();
		String resource = "MyBatisConfiguration.xml";

		try {
			Reader reader = Resources.getResourceAsReader(resource);

			// this will load the default environment configured in
			// Configuration.xml
			SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
					.build(reader);

			// set the sqlSessionFactory as an application scoped variable
			ctx.setAttribute("sqlSessionFactory", sqlSessionFactory);
		} catch (Exception e) {
			System.out.println("FATAL ERROR: myBatis could not be initialized");
			System.out.println(e);
			System.exit(1);
		}
	}

	public void contextDestroyed(ServletContextEvent paramServletContextEvent) {
		// nothing to do here right now
	}
}
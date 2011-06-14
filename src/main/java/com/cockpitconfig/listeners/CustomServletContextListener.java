package com.cockpitconfig.listeners;

import java.io.Reader;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class CustomServletContextListener implements ServletContextListener {

 public void contextInitialized(ServletContextEvent event) {

	 ServletContext ctx = event.getServletContext();
     String resource = "Configuration.xml";

     try {
    	 Reader reader = Resources.getResourceAsReader(resource);
    	 SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader); //this will load the default environment configured in Configuration.xml
    	 ctx.setAttribute("sqlSessionFactory", sqlSessionFactory); //set the sqlSessionFactory as an application scoped variable
     } catch(Exception e){
    	 System.out.println("FATAL ERROR: myBatis could not be initialized");
    	 System.out.println(e);
    	 System.exit(1);
    }
 }

 public void contextDestroyed(ServletContextEvent paramServletContextEvent) {

	 //nothing to do here right now ek se
 }
}
package com.cockpitconfig.listeners;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class CamelContextListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent event) {

		ServletContext scxt = event.getServletContext();

	}

	public void contextDestroyed(ServletContextEvent paramServletContextEvent) {
		// nothing to do here right now
	}
}
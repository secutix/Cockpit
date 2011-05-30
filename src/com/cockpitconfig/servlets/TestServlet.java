package com.cockpitconfig.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cockpitconfig.objects.*;
import com.cockpitconfig.db.*;

public class TestServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;

	private static Logger log = LoggerFactory.getLogger(TestServlet.class);

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
	 System.out.println("I am at testSevlet.java");

	 SqlSessionFactory sf = (SqlSessionFactory)getServletContext().getAttribute("sqlSessionFactory");
     CockpitConfigDAO dao = new CockpitConfigDAO(sf);

  try
  {
   ArrayList<Communication> clients = dao.getClients();

   request.setAttribute("all_clients", clients);

  }
  catch(PersistenceException p)
  {
   p.printStackTrace();
  }
  finally
  {
   request.setAttribute("message","Welcome to the modeling agency MyBatis tutorial");
   log.info("The message attribute has been set");
  }


  RequestDispatcher view = request.getRequestDispatcher("index.jsp");
  view.forward(request, response);
 }

 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  doPost(request,response);
 }

}
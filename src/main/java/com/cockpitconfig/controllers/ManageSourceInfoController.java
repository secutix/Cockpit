package com.cockpitconfig.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.cockpitconfig.db.SourcesDAO;
import com.cockpitconfig.objects.Sources;

public class ManageSourceInfoController extends AbstractController{

	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("ManageSourceInfo", "System", "S");

        String selectedSource = request.getParameter("selectedSource");
        String sourceUrl = request.getParameter("sourceUrl");
        String sourceDescription = request.getParameter("sourceDescription");

        if (sourceUrl != null && sourceDescription != null) {
        	addNewSource(sourceUrl, sourceDescription);
        }

        if(selectedSource != null) {
        	removeSelectedSource (selectedSource);
        }

        return mav;				// return modelandview object
    }

	private void addNewSource (String sourceUrl, String sourceDescription) {

		SqlSessionFactory sf = (SqlSessionFactory)getServletContext().getAttribute("sqlSessionFactory");
		SourcesDAO sourcesDao = new SourcesDAO(sf);

		Sources newSource = new Sources();
		newSource.setUrl(sourceUrl);
		newSource.setDescription(sourceDescription);
		sourcesDao.addSource(newSource);
	}

	private void removeSelectedSource (String selectedSource) {
		SqlSessionFactory sf = (SqlSessionFactory)getServletContext().getAttribute("sqlSessionFactory");
		SourcesDAO sourcesDao = new SourcesDAO(sf);

		sourcesDao.removeSelectedSource(selectedSource);
	}
}
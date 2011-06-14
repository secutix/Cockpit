package com.cockpitconfig.controllers;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import org.apache.ibatis.session.SqlSessionFactory;

import com.cockpitconfig.db.SourcesDAO;
import com.cockpitconfig.objects.Sources;

public class ManageSourceController extends AbstractController {

	@Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("ManageSource","Streams", "A"); //TODO: last argument is message
        handleFormSubmission (request, mav);

        return mav;				// return modelandview object
    }

	/**
	 * Function which sends back values from Server Side to Client side using ModelAndView object
	 * @param request 	HttpServletRequest Object
	 * @param mav 		ModelAndView Object
	 */
    private void handleFormSubmission(HttpServletRequest request, ModelAndView mav) {

    	SqlSessionFactory sf = (SqlSessionFactory)getServletContext().getAttribute("sqlSessionFactory");

    	SourcesDAO sourcesDao= new SourcesDAO(sf);
    	ArrayList<Sources> sources = sourcesDao.getSources();
    	String[] sourceUrlTemp = new String [sources.size()];
    	for (int i = 0; i < sources.size(); ++i) {
    		sourceUrlTemp[i] = sources.get(i).getUrl();
    	}

    	mav.addObject("sourceUrl", sourceUrlTemp);
    }
}
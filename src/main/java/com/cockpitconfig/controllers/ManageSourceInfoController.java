package com.cockpitconfig.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.cockpitconfig.db.SourcesDAO;

public class ManageSourceInfoController extends AbstractController{

	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("ManageSourceInfo", "System", "S");

        String selectedUrl = request.getParameter("selectedUrl");

        if(selectedUrl != null) {
        	removeSelectedSource (selectedUrl);
        }

        return mav;				// return modelandview object
    }

	private void removeSelectedSource (String selectedUrl) {
		SqlSessionFactory sf = (SqlSessionFactory)getServletContext().getAttribute("sqlSessionFactory");
		SourcesDAO sourcesDao = new SourcesDAO(sf);

		sourcesDao.removeSelectedSource(selectedUrl);
	}
}
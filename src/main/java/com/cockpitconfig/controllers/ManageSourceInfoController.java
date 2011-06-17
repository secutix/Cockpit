package com.cockpitconfig.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.cockpitconfig.db.AssertionGroupDAO;
import com.cockpitconfig.db.SourcesDAO;
import com.cockpitconfig.objects.Sources;

public class ManageSourceInfoController extends AbstractController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView("ManageSourceInfo", "System", "S");

		String selectedSource = request.getParameter("selectedSource");
		String sourceUrl = request.getParameter("sourceUrl");
		String sourceDescription = request.getParameter("sourceDescription");

		if (sourceUrl != null && sourceDescription != null) {
			addNewSource(sourceUrl, sourceDescription);
		}

		if (selectedSource != null) {
			removeSelectedSource(selectedSource, response);
		}

		return mav; // return modelandview object
	}

	private void addNewSource(String sourceUrl, String sourceDescription) {

		SqlSessionFactory sf = (SqlSessionFactory) getServletContext()
				.getAttribute("sqlSessionFactory");
		SourcesDAO sourcesDao = new SourcesDAO(sf);

		Sources newSource = new Sources();
		newSource.setUrl(sourceUrl);
		newSource.setDescription(sourceDescription);
		sourcesDao.addSource(newSource);
	}

	private void removeSelectedSource(String selectedSource,
			HttpServletResponse response) throws Exception {
		SqlSessionFactory sf = (SqlSessionFactory) getServletContext()
				.getAttribute("sqlSessionFactory");
		SourcesDAO sourcesDao = new SourcesDAO(sf);

		// Check whether url to be removed exists or not in AssertionGroup
		// Table. If yes, warn the user else remove the url
		AssertionGroupDAO agDao = new AssertionGroupDAO(sf);
		String ifExists = agDao.checkForSource(sourcesDao
				.getPKForSource(selectedSource));
		if (ifExists == null) {
			sourcesDao.removeSelectedSource(selectedSource);
		} else {
			response.setContentType("application/json;charset=UTF-8");
			response.setHeader("Cache-Control", "no-cache");
			JSONObject jsonResult = new JSONObject();

			jsonResult.put("ruleName", ifExists);

			response.getWriter().write(jsonResult.toString());
			response.getWriter().close();
		}

	}
}
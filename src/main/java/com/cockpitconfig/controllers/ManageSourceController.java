package com.cockpitconfig.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.cockpitconfig.db.SourcesDAO;
import com.cockpitconfig.objects.Sources;

public class ManageSourceController extends AbstractController{

	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("ManageSource", "System", "S");

        String start = request.getParameter("start");
        String limit = request.getParameter("limit");

        setResponse (response, Integer.parseInt(start), Integer.parseInt(limit));

        return mav;				// return modelandview object
    }

	private void setResponse (HttpServletResponse response, int start, int limit) throws Exception {
        HashMap param = new HashMap();

        param.put("start", start);
        param.put("limit", limit);

        SqlSessionFactory sf = (SqlSessionFactory)getServletContext().getAttribute("sqlSessionFactory");
        SourcesDAO tempSources = new SourcesDAO(sf);
        ArrayList <Sources> sources = tempSources.getAllSources(param);

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        JSONObject jsonResult = new JSONObject();
        JSONArray jsonItems = new JSONArray();

        for (int i = 0; i < sources.size(); ++i) {
            JSONObject json = sources.get(i).toJSON();
			jsonItems.add(json);
        }

        jsonResult.put("topics", jsonItems);
        jsonResult.put("totalCount", Integer.toString(tempSources.getTotalSourceCount()));

    	response.getWriter().write(jsonResult.toString());
        response.getWriter().close();
	}
}
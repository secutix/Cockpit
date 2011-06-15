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

import com.cockpitconfig.db.NotificationOccurrencesDAO;
import com.cockpitconfig.objects.NotificationOccurrence;

public class RecentActivityController extends AbstractController{

	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("RecentActivity", "System", "S");

        String start = request.getParameter("start");
        String limit = request.getParameter("limit");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");

        setResponse (response, Integer.parseInt(start), Integer.parseInt(limit), fromDate, toDate );

        return mav;				// return modelandview object
    }

	private void setResponse (HttpServletResponse response, int start, int limit, String fromDate, String toDate) throws Exception {
        HashMap param = new HashMap();

        if (fromDate != null && fromDate.isEmpty()) {
        	fromDate = null;
        }

        if (toDate != null && toDate.isEmpty()) {
        	toDate = null;
        }

        param.put("fromdate", fromDate);
        param.put("todate", toDate);
        param.put("start", start);
        param.put("limit", limit);

        SqlSessionFactory sf = (SqlSessionFactory)getServletContext().getAttribute("sqlSessionFactory");
        NotificationOccurrencesDAO tempNotiOccu = new NotificationOccurrencesDAO(sf);
        ArrayList <NotificationOccurrence> notificationOccurrences = tempNotiOccu.getAlerts(param);

        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        JSONObject jsonResult = new JSONObject();
        JSONArray jsonItems = new JSONArray();

        for (int i = 0; i < notificationOccurrences.size(); ++i) {
            JSONObject json = notificationOccurrences.get(i).toJSON();
			jsonItems.add(json);
        }

        jsonResult.put("topics", jsonItems);
        jsonResult.put("totalCount", Integer.toString(tempNotiOccu.getTotalCount(param)));

    	response.getWriter().write(jsonResult.toString());
        response.getWriter().close();
	}
}
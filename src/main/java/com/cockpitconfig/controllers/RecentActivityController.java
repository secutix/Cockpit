package com.cockpitconfig.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.management.Notification;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.cockpitconfig.db.CommunicationViaEmailDAO;
import com.cockpitconfig.db.NotificationOccurrencesDAO;
import com.cockpitconfig.objects.CommunicationViaEmail;
import com.cockpitconfig.objects.NotificationOccurrences;

public class RecentActivityController extends AbstractController{

	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("RecentActivity", "System", "S");

        String start = request.getParameter("start");
        String limit = request.getParameter("limit");
        String fromDate = request.getParameter("fromDate");
        String toDate = request.getParameter("toDate");

/*/*        String str_date="11-June-07";
       DateFormat formatter ;
        Date date ;
        formatter = new SimpleDateFormat("dd-MMM-yy");
        date = (Date)formatter.parse(str_date);
        Date startDate = "2011-06-07";
        Date endDate =

        HashMap<String, Date> param = new HashMap();

        param.put("startdate", );
        param.put("enddate", Integer.parseInt(limit));*/


        System.out.println(start);
        System.out.println(limit);
        System.out.println(fromDate);
        System.out.println(toDate);

        setResponse (response, Integer.parseInt(start), Integer.parseInt(limit), fromDate, toDate );

        return mav;				// return modelandview object
    }

	private void setResponse (HttpServletResponse response, int start, int limit, String fromDate, String toDate) throws Exception {

        HashMap param = new HashMap();

        param.put("startdate", start);
        param.put("enddate", limit);

        SqlSessionFactory sf = (SqlSessionFactory)getServletContext().getAttribute("sqlSessionFactory");
        NotificationOccurrencesDAO tempNotiOccu = new NotificationOccurrencesDAO(sf);
        ArrayList <NotificationOccurrences> notificationOccurrences = tempNotiOccu.getAlerts(param);

		response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        JSONObject jsonResult = new JSONObject();


        JSONArray jsonItems = new JSONArray();
        //for (Iterator iter = results.iterator(); iter.hasNext();) {
        for (int i = 0; i < notificationOccurrences.size(); ++i) {
            JSONObject json = notificationOccurrences.get(i).toJSON();
			jsonItems.add(json);
        }

        jsonResult.put("topics", jsonItems);
        jsonResult.put("totalCount", "3");//Integer.toString(tempNotiOccu.getTotalCount()));
        //jsonResult.put("existingMinVal", existingMinVal);

    	response.getWriter().write(jsonResult.toString());
        response.getWriter().close();
	}
}
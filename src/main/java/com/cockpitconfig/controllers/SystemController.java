package com.cockpitconfig.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class SystemController extends AbstractController{

	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("SystemInfo", "System", "S");

        mav.addObject("JVMVendor", System.getProperty("java.vendor"));
        mav.addObject("JVMVersion", System.getProperty("java.version"));
        mav.addObject("JVMVendorURL", System.getProperty("java.vendor.url"));
        mav.addObject("OSName", System.getProperty("os.name"));
        mav.addObject("OSVersion", System.getProperty("os.version"));
        mav.addObject("OSArchitecture", System.getProperty("os.arch"));

        return mav;				// return modelandview object
    }
}

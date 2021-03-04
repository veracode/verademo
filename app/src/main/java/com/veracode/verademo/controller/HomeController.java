package com.veracode.verademo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@Controller
public class HomeController {
	private static final Logger logger = LogManager.getLogger("VeraDemo:HomeController");

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String renderGet(Model model, HttpServletRequest request)
	{
		// Check if user is already logged in
		if (request.getSession().getAttribute("username") != null) {
			// default to user's feed
			return "redirect:feed";
		}

		model.addAttribute("javax.servlet.forward.request_uri", "/login");
		model.addAttribute("username", "");
		return "login";
	}
}

package com.veracode.verademo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.veracode.verademo.utils.Utils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String renderGet(Model model, HttpServletRequest request, HttpServletResponse response) {
		// Check if user is already logged in
		if (Utils.getSessionUserName(request, response) != null) {
			// Redirect to user's feed
			return Utils.redirect("feed");
		}

		model.addAttribute("javax.servlet.forward.request_uri", "/login");
		model.addAttribute("username", "");

		return "login";
	}
}

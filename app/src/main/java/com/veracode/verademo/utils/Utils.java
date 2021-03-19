package com.veracode.verademo.utils;

import java.util.Collection;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;

public class Utils {
	private static String getApplicationUrl() {
		return System.getenv("APPLICATION_URL");
	}

	public static String getSessionUserName(HttpServletRequest request, HttpServletResponse response) {
		String username = (String) request.getSession().getAttribute("username");

		if (getApplicationUrl() != null) {
			upgradeCookieSecurityForHttps(response);
		}
		
		return username;
	}

	public static void setSessionUserName(HttpServletRequest request, HttpServletResponse response, String value) {
		request.getSession().setAttribute("username", value);

		if (getApplicationUrl() != null) {
			upgradeCookieSecurityForHttps(response);
		}
	}

	private static void upgradeCookieSecurityForHttps(HttpServletResponse response) {
		Collection<String> headers = response.getHeaders(HttpHeaders.SET_COOKIE);
		boolean firstHeader = true;
		for (String header : headers) {
			// there can be multiple Set-Cookie attributes
			if (firstHeader) {
				response.setHeader(HttpHeaders.SET_COOKIE, String.format("%s; SameSite=None; Secure;", header));
				firstHeader = false;
				continue;
			}
			response.addHeader(HttpHeaders.SET_COOKIE, String.format("%s; SameSite=None; Secure;", header));
		}
	}

	public static void setUsernameCookie(HttpServletResponse response, String value) {
		setCookie(response, "username", value);
	}

	public static void setCookie(HttpServletResponse response, String name, String value) {
		response.addCookie(new Cookie(name, value));

		if (getApplicationUrl() != null) {
			upgradeCookieSecurityForHttps(response);
		}
	}

	public static String redirect(String destination) {
		if (getApplicationUrl() != null) {
			return "redirect:" + getApplicationUrl() + "/" + destination;
		}

		return "redirect:" + destination;
	}
}

package com.veracode.verademo.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletContext;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Scope("request")
public class ToolsController {
	private static final Logger logger = LogManager.getLogger("VeraDemo:BlabController");
	
	@Autowired
	ServletContext context;

	@RequestMapping(value = "/tools", method = RequestMethod.GET)
	public String tools()
	{
		return "tools";
	}

	@RequestMapping(value = "/tools", method = RequestMethod.POST)
	public String tools(
			@RequestParam(value = "host", required = false) String host,
			@RequestParam(value = "fortunefile", required = false) String fortuneFile,
			Model model)
	{
		model.addAttribute("ping", host != null ? ping(host) : "");

		if (fortuneFile == null) {
			fortuneFile = "funny.txt";
		}
		model.addAttribute("fortunes", fortune(fortuneFile));

		return "tools";
	}

	private String ping(String host)
	{
		String output = "";
		Process proc;
		try {
			/* START BAD CODE */
			if (System.getProperty("os.name").startsWith("Windows")) {
				proc = Runtime.getRuntime().exec("cmd.exe /c ping " + host);
			}
			else {
				proc = Runtime.getRuntime().exec("ping " + host);
			}
			/* END BAD CODE */

			InputStreamReader isr = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(isr);

			String line;

			while ((line = br.readLine()) != null) {
				output += line + "\n";
			}
		}
		catch (IOException ex) {
			logger.error(ex);
		}
		return output;
	}

	private String fortune(String fortuneFile)
	{
		String cmd = context.getRealPath("\\resources\\bin\\fortune-go.exe") + " "
				+ context.getRealPath("\\resources\\bin\\" + fortuneFile);

		String output = "";
		Process proc;
		try {
			/* START BAD CODE */
			if (System.getProperty("os.name").startsWith("Windows")) {
				proc = Runtime.getRuntime().exec(new String[] { "cmd.exe", "/c", cmd });
			}
			else {
				proc = Runtime.getRuntime().exec(cmd);
			}
			/* END BAD CODE */

			InputStreamReader isr = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(isr);

			String line;

			while ((line = br.readLine()) != null) {
				output += line + "\n";
			}
		}
		catch (IOException ex) {
			logger.error(ex);
		}
		return output;
	}
}

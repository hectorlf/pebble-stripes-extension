package com.hectorlopezfernandez.pebblestripes;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.LoaderException;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

@WebServlet(urlPatterns={"*.pebble"},loadOnStartup=4)
public class PebbleServlet implements Servlet {

	private final static Logger logger = LoggerFactory.getLogger(PebbleServlet.class);

	private ServletConfig servletConfig;
	private PebbleEngine engine;

	
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		this.servletConfig = servletConfig;
		// Engine creation
		logger.debug("Pebble engine initialization");
		Loader<String> l = new ClasspathLoader();
		StripesExtension se = new StripesExtension();
		engine = new PebbleEngine.Builder().loader(l).extension(se).build();
	}

	@Override
	public void destroy() {
		engine = null;
	}

	
	@Override
	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	@Override
	public String getServletInfo() {
		return "PebbleServlet";
	}


	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		logger.debug("Entering PebbleServlet.service");
		
		HttpServletRequest request = (HttpServletRequest)arg0;
		HttpServletResponse response = (HttpServletResponse)arg1;
		
		// fill the execution context with request attributes
		Map<String, Object> context = new HashMap<String,Object>();
		context.put(StripesExtension.CONTEXT_PATH, request.getServletContext().getContextPath());
		
        // write the response headers
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        
        // process template
        PebbleTemplate template = null;
        try {
        	String templatePath = request.getServletPath();
        	if (templatePath.startsWith("/")) templatePath = templatePath.substring(1);
        	template = engine.getTemplate(templatePath);
        } catch (LoaderException le) {
        	// template not found, log to debug and return 404
        	logger.debug("Loader was unable to find the template: {}", request.getServletPath());
        	response.sendError(HttpServletResponse.SC_NOT_FOUND);
        	return;
        } catch (PebbleException pe) {
        	// error parsing template, log to error and return 500
        	logger.error("Error parsing template {}, stacktrace follows", request.getServletPath());
        	pe.printStackTrace();
        	response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        	return;
        }
        try {
        	template.evaluate(response.getWriter(), context, request.getLocale());
        } catch (PebbleException pe) {
        	// error processing template, log to error and return 500
        	logger.error("Error processing template {}, stacktrace follows", request.getServletPath());
        	pe.printStackTrace();
        	response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        	return;
        } catch (IOException ioe) {
        	// probably a closed connection, log to debug and nothing more to do
        	logger.debug("Error writing template {} to output with exception: {}", request.getServletPath(), ioe.getMessage());
        	return;
        }
	}

}
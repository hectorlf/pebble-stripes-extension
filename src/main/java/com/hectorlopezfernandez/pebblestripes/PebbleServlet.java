package com.hectorlopezfernandez.pebblestripes;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.LoaderException;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.loader.ServletLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

public class PebbleServlet implements Servlet {

	private final static Logger logger = LoggerFactory.getLogger(PebbleServlet.class);

	private ServletConfig servletConfig;
	private PebbleEngine engine;

	
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		this.servletConfig = servletConfig;
		// Engine creation
		logger.debug("Pebble engine initialization");
		PebbleEngine.Builder engineBuilder = new PebbleEngine.Builder();
		Loader<String> l = new ServletLoader(servletConfig.getServletContext());
		StripesExtension se = new StripesExtension();
		engineBuilder.loader(l).extension(se);
		// allow to customize engine
		configureEngine(engineBuilder, servletConfig);
		engine = engineBuilder.build();
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
		logger.debug("Entering PebbleServlet.service()");
		
		HttpServletRequest request = (HttpServletRequest)arg0;
		HttpServletResponse response = (HttpServletResponse)arg1;
		
		// execution context is backed by a delegating map to be able to access variables
		// stored in HttpServletRequest's attribute map
		// also, request and response objects are added as a bonus
		Map<String, Object> context = new HttpServletRequestMapAdapter(request);
		context.put(StripesExtension.HTTP_SERVLET_REQUEST, request);
		context.put(StripesExtension.HTTP_SERVLET_RESPONSE, response);
		
        // write the response headers
        if (response.getContentType() == null) response.setContentType("text/html;charset=UTF-8");
        if (!response.containsHeader("Pragma")) response.setHeader("Pragma", "no-cache");
        if (!response.containsHeader("Cache-Control")) response.setHeader("Cache-Control", "no-cache");
        if (!response.containsHeader("Expires")) response.setDateHeader("Expires", 0);
        
        // load template
        PebbleTemplate template = null;
        String templatePath = parseTemplatePath(request.getServletPath());
        try {
        	template = engine.getTemplate(templatePath);
        } catch (LoaderException le) {
        	// template not found, log to debug and return 404
        	logger.debug("Loader was unable to find the template '{}' from servlet path '{}'", templatePath, request.getServletPath());
        	response.sendError(HttpServletResponse.SC_NOT_FOUND);
        	return;
        } catch (PebbleException pe) {
        	// error parsing template, log to error and return 500
        	logger.error("Error parsing template '{}', stacktrace follows", templatePath);
        	pe.printStackTrace();
        	response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        	return;
        }
        // evaluate template
        try {
        	template.evaluate(response.getWriter(), context, request.getLocale());
        } catch (PebbleException pe) {
        	// error processing template, log to error and return 500
        	logger.error("Error processing template '{}', stacktrace follows", templatePath);
        	pe.printStackTrace();
        	response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        	return;
        } catch (IOException ioe) {
        	// probably a closed connection, log to debug and nothing more to do
        	logger.debug("Error writing template '{}' to output with exception: {}", templatePath, ioe.getMessage());
        	return;
        }
	}

	protected void configureEngine(PebbleEngine.Builder builder, ServletConfig servletConfig) {
		// empty by default
	}

	protected String parseTemplatePath(String servletPath) {
		return servletPath;
	}

}
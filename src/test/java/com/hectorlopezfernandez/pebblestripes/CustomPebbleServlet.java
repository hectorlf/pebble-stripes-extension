package com.hectorlopezfernandez.pebblestripes;

import javax.servlet.ServletConfig;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.Loader;

public class CustomPebbleServlet extends PebbleServlet {

	protected void configureEngine(PebbleEngine.Builder builder, ServletConfig servletConfig) {
		Loader<String> l = new ClasspathLoader();
		builder.loader(l);
	}

	protected String parseTemplatePath(String servletPath) {
		if (servletPath.startsWith("/")) servletPath = servletPath.substring(1);
		return servletPath;
	}

}
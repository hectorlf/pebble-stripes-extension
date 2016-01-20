package com.hectorlopezfernandez.pebblestripes;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ClasspathLoader;
import com.mitchellbosecke.pebble.loader.Loader;

public class CustomPebbleServlet extends PebbleServlet {

	@Override
	protected void configureEngine(PebbleEngine.Builder builder, ServletConfig servletConfig) {
		Loader<String> l = new ClasspathLoader();
		builder.loader(l);
	}

	@Override
	protected String parseTemplatePath(String originalPath, HttpServletRequest request, HttpServletResponse response) {
		if (originalPath.startsWith("/")) originalPath = originalPath.substring(1);
		return originalPath;
	}

}
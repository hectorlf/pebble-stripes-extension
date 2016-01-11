package com.hectorlopezfernandez.pebblestripes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mitchellbosecke.pebble.extension.AbstractExtension;
import com.mitchellbosecke.pebble.extension.Filter;
import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;

public class StripesExtension extends AbstractExtension {

	public static final String CONTEXT_PATH = "servletContext.contextPath";
	
	private final UrlFunction urlFunction;

    public StripesExtension() {
    	this.urlFunction = new UrlFunction();
    }

    @Override
    public Map<String, Filter> getFilters() {
        Map<String, Filter> filters = new HashMap<>(2);
        return filters;
    }
    
    @Override
    public Map<String, Function> getFunctions() {
    	Map<String, Function> functions = new HashMap<>(2);
    	functions.put(UrlFunction.FUNCTION_NAME, urlFunction);
        return functions;
    }

    @Override
    public List<TokenParser> getTokenParsers() {
        List<TokenParser> parsers = new ArrayList<>(6);
        return parsers;
    }

}
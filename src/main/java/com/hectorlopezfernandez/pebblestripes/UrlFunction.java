package com.hectorlopezfernandez.pebblestripes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.ScopeChain;

import net.sourceforge.stripes.util.UrlBuilder;

public class UrlFunction implements Function {

    private final List<String> argumentNames = new ArrayList<>();

	public static final String FUNCTION_NAME = "stripesUrl";

    public UrlFunction() {
        argumentNames.add("class");
        argumentNames.add("params");
        argumentNames.add("anchor");
        argumentNames.add("value");
        argumentNames.add("event");
        argumentNames.add("prependContext");
    }

    @Override
    public List<String> getArgumentNames() {
        return argumentNames;
    }

    @Override
    public Object execute(Map<String, Object> args) {
        EvaluationContext context = (EvaluationContext) args.get("_context");
        ScopeChain values = context.getScopeChain();

        // process class and url
    	String baseUrl = ResolveUtils.resolveBaseUrl(args.get("class"), args.get("value"));
        // process url parameters
    	Map<Object,Object> parameters = ResolveUtils.resolveParameters(args.get("params"));
        // process anchor
    	String anchor = ResolveUtils.resolveAnchor(args.get("anchor"));
        // process event
    	String event = ResolveUtils.resolveEvent(args.get("event"));

    	// build url
        UrlBuilder builder = new UrlBuilder(context.getLocale(), baseUrl, false);
        builder.addParameters(parameters);
        if (anchor != null) builder.setAnchor(anchor);
        if (event != null) builder.setEvent(event);
        String computedUrl = builder.toString();
        
        // prepend context if needed
        String contextPath = ResolveUtils.resolveContextPath(values);
        Boolean prependContext = ResolveUtils.resolvePrependContext(args.get("prependContext"));
        boolean prepend = (prependContext == null && computedUrl.startsWith("/") && !computedUrl.startsWith(contextPath)) || prependContext;
        if (prepend) computedUrl = contextPath + computedUrl;

		return computedUrl;
    }

}
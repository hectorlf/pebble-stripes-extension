package com.hectorlopezfernandez.pebblestripes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.mitchellbosecke.pebble.extension.Function;
import com.mitchellbosecke.pebble.template.EvaluationContext;
import com.mitchellbosecke.pebble.template.ScopeChain;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.util.ReflectUtil;
import net.sourceforge.stripes.util.UrlBuilder;

public class UrlFunction implements Function {

	private static final String FUNCTION_NAME = "stripesUrl";
    private final List<String> argumentNames = new ArrayList<>();

    public UrlFunction() {
        argumentNames.add("class");
        argumentNames.add("params");
        argumentNames.add("anchor");
        argumentNames.add("url");
        argumentNames.add("event");
        argumentNames.add("addSourcePage");
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

        // resolve timezone, it's used in both parsing and creating datetimes
		DateTimeZone timezone = ResolveUtils.resolveTimezone(args.get("timezone"), values);

    	// process datetime
    	Object value = args.get("value");
    	DateTime d = null;
    	if (value == null) {
    		d = new DateTime(timezone);
        } else if (value instanceof Date) {
    		d = new DateTime((Date) value, timezone);
        } else if (value instanceof String) {
        	DateTimeFormatter formatter;
        	Object patternParam = args.get("pattern");
        	Object styleParam = args.get("style");
        	// if pattern is specified, it is preferred to style; but if not specified, style is preferred to default joda pattern
        	if (patternParam == null && styleParam != null) {
        		formatter = DateTimeFormat.forStyle(styleParam.toString());
        	} else {
        		String pattern = ResolveUtils.resolvePattern(patternParam, values);
        		if (pattern != null) {
                    formatter = DateTimeFormat.forPattern(pattern);
                } else {
                    formatter = DateTimeFormat.fullDateTime();
                }
        	}
            formatter = formatter.withLocale(ResolveUtils.resolveLocale(args.get("locale"), context));
            formatter = formatter.withZone(timezone);
            d = formatter.parseDateTime((String) value);
        }
    	return d;
    }

    private String buildUrl() {
        HttpServletRequest request = (HttpServletRequest) getPageContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) getPageContext().getResponse();


        // Add all the parameters and reset the href attribute; pass to false here because
        // the HtmlTagSupport will HtmlEncode the ampersands for us
        String base = null;
        // If the beanclass attribute was supplied we'll prefer that to an href
        if (this.beanclass != null) {
            String beanHref = getActionBeanUrl(beanclass);
            if (beanHref == null) {
                throw new StripesJspException("The value supplied for the 'beanclass' attribute "
                        + "does not represent a valid ActionBean. The value supplied was '" +
                        this.beanclass + "'. If you're prototyping, or your bean isn't ready yet " +
                        "and you want this exception to go away, just use 'href' for now instead.");
            }
            else {
                return beanHref;
            }
        }
        else {
            return getUrl();
        }

        UrlBuilder builder = new UrlBuilder(pageContext.getRequest().getLocale(), base, false);
        if (this.event != VALUE_NOT_SET) {
            builder.setEvent(this.event == null || this.event.length() < 1 ? null : this.event);
        }
        if (addSourcePage) {
            builder.addParameter(StripesConstants.URL_KEY_SOURCE_PAGE,
                    CryptoUtil.encrypt(request.getServletPath()));
        }
        if (this.anchor != null) {
            builder.setAnchor(anchor);
        }
        builder.addParameters(this.parameters);

        // Prepend the context path, but only if the user didn't already
        String url = builder.toString();
        String contextPath = request.getContextPath();
        if (contextPath.length() > 1) {
            boolean prepend = prependContext != null && prependContext
                    || prependContext == null && beanclass != null
                    || prependContext == null && url.startsWith("/") && !url.startsWith(contextPath);

            if (prepend) {
                if (url.startsWith("/"))
                    url = contextPath + url;
                else
                    log.warn("Use of prependContext=\"true\" is only valid with a URL that starts with \"/\"");
            }
        }

        return response.encodeURL(url);
    }

}
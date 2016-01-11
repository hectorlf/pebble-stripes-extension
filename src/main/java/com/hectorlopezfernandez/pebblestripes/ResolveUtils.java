package com.hectorlopezfernandez.pebblestripes;

import java.util.Collections;
import java.util.Map;

import com.mitchellbosecke.pebble.template.ScopeChain;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.util.ReflectUtil;

final class ResolveUtils {

	private ResolveUtils() {
		// not instantiable
	}

	public static String resolveBaseUrl(Object beanClass, Object url) {
    	// beanClass is preferred over url
    	if (beanClass != null) {
    		String base = null;
    		if (beanClass instanceof Class<?>) {
    			base = resolveActionBeanUrl((Class<?>)beanClass);
    		} else if (beanClass instanceof String) {
        		try {
    				base = resolveActionBeanUrl((String)beanClass);
    			} catch (ClassNotFoundException e) {
    				throw new IllegalArgumentException("The class argument " + beanClass + " was not found on the classpath.");
    			}
    		} else {
    			throw new IllegalArgumentException("Only Class and String types are supported for the class argument. Actual type: " + beanClass.getClass().getName());
    		}
    		return base;
    	} else if (url != null) {
    		if (!(url instanceof String)) throw new IllegalArgumentException("The url argument was not of type String. Actual type: " + url.getClass().getName());
    		return url.toString();
    	} else {
    		throw new IllegalArgumentException("Both class and url arguments were null. You must specify one at least.");
    	}
	}

    @SuppressWarnings("unchecked")
	public static String resolveActionBeanUrl(Class<?> beanClass) {
    	if (beanClass == null || !ActionBean.class.isAssignableFrom(beanClass)) throw new IllegalArgumentException("Cannot infer ActionBean type from value: " + beanClass == null ? "null" : beanClass.getClass().getName());
        Class<? extends ActionBean> beanType = (Class<? extends ActionBean>) beanClass;
        return StripesFilter.getConfiguration().getActionResolver().getUrlBinding(beanType);
    }

    @SuppressWarnings("unchecked")
	public static String resolveActionBeanUrl(String className) throws ClassNotFoundException {
    	Class<?> beanClass = ReflectUtil.findClass(className);
    	if (beanClass == null || !ActionBean.class.isAssignableFrom(beanClass)) throw new IllegalArgumentException("Cannot infer ActionBean type from value: " + beanClass == null ? "null" : beanClass.getClass().getName());
        Class<? extends ActionBean> beanType = (Class<? extends ActionBean>) beanClass;
        return StripesFilter.getConfiguration().getActionResolver().getUrlBinding(beanType);
    }

    @SuppressWarnings("unchecked")
	public static Map<Object,Object> resolveParameters(Object parameters) {
    	if (parameters == null) {
    		return Collections.emptyMap();
    	} else if (parameters instanceof Map) {
    		return (Map<Object,Object>) parameters;
    	} else {
    		throw new IllegalArgumentException("Only Map types are supported for the params argument. Actual type: " + parameters.getClass().getName());
    	}
    }

	public static String resolveAnchor(Object anchor) {
    	if (anchor == null) {
    		return null;
    	} else if (anchor instanceof String) {
    		return (String) anchor;
    	} else {
    		throw new IllegalArgumentException("Only String types are supported for the anchor argument. Actual type: " + anchor.getClass().getName());
    	}
    }

	public static String resolveEvent(Object event) {
    	if (event == null) {
    		return null;
    	} else if (event instanceof String) {
    		return (String) event;
    	} else {
    		throw new IllegalArgumentException("Only String types are supported for the event argument. Actual type: " + event.getClass().getName());
    	}
    }

	public static String resolveContextPath(ScopeChain values) {
		String path = (String) values.get(StripesExtension.CONTEXT_PATH);
		if (path == null || path.length() == 0) throw new IllegalStateException("Couldn't find the context path in the scope chain. Every template execution must have the servlet context's context path injected into the ");
    	return path;
    }

	public static Boolean resolvePrependContext(Object prependContext) {
		if (prependContext == null) {
    		return null;
    	} else if (prependContext instanceof String) {
    		return Boolean.valueOf((String)prependContext);
    	} else if (prependContext instanceof Boolean) {
    		return (Boolean) prependContext;
    	} else {
    		throw new IllegalArgumentException("Only Boolean and String types are supported for the prependContext argument. Actual type: " + prependContext.getClass().getName());
    	}
    }

}
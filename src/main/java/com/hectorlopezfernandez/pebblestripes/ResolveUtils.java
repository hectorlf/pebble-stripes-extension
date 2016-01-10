package com.hectorlopezfernandez.pebblestripes;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.util.ReflectUtil;

final class ResolveUtils {

	private ResolveUtils() {
		// not instantiable
	}

    /**
     * Helper method that takes an attribute which may be either a String class name
     * or a Class object and returns the Class representing the appropriate ActionBean.
     * If for any reason the Class cannot be determined, or it is not an ActionBean, null
     * will be returned instead.
     *
     * @param nameOrClass either the String FQN of an ActionBean class, or a Class object
     * @return the appropriate ActionBean class or null
     * @throws ClassNotFoundException 
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Class<? extends ActionBean> resolveActionBeanType(Object nameOrClass) throws ClassNotFoundException {
        Class result = null;

        // Figure out if it's a String of Class (or something else?) and act appropriately
        if (nameOrClass instanceof String) {
            result = ReflectUtil.findClass((String) nameOrClass);
        } else if (nameOrClass instanceof Class) {
            result = (Class) nameOrClass;
        } else {
        	throw new IllegalArgumentException("The value supplied to getActionBeanType() was neither a String nor a Class. Cannot infer ActionBean type from value: " + nameOrClass);
        }

        // And for good measure, let's make sure it's an ActionBean implementation!
        if (ActionBean.class.isAssignableFrom(result)) {
            return result;
        } else {
        	throw new IllegalArgumentException("Class '" + result.getName() + "' does not implement ActionBean.");
        }
    }

    /**
     * Similar to the getActionBeanType(Object) method except that instead of
     * returning the Class of ActionBean it returns the URL Binding of the ActionBean.
     *
     * @param nameOrClass either the String FQN of an ActionBean class, or a Class object
     * @return the URL of the appropriate ActionBean class or null
     * @throws ClassNotFoundException 
     */
    public static String resolveActionBeanUrl(Object nameOrClass) throws ClassNotFoundException {
        Class<? extends ActionBean> beanType = resolveActionBeanType(nameOrClass);
        return StripesFilter.getConfiguration().getActionResolver().getUrlBinding(beanType);
    }

}
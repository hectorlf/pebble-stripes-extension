package com.hectorlopezfernandez.pebblestripes.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;

public abstract class BaseAction implements ActionBean {

	private ActionBeanContext context;

	@Override
	public void setContext(ActionBeanContext context) {
		this.context = context;
	}

	@Override
	public ActionBeanContext getContext() {
		return context;
	}

}
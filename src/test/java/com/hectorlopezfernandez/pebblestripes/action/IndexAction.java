package com.hectorlopezfernandez.pebblestripes.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/index.action")
public class IndexAction implements ActionBean {

	private ActionBeanContext context;

	@Override
	public void setContext(ActionBeanContext context) {
		this.context = context;
	}

	@Override
	public ActionBeanContext getContext() {
		return context;
	}

	@DefaultHandler
	public Resolution execute() {
		return new StreamingResolution("text/plain", "index!");
	}

}
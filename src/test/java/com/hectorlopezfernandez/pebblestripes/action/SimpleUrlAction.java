package com.hectorlopezfernandez.pebblestripes.action;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/simple-url.action")
public class SimpleUrlAction implements ActionBean {

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
		return new ForwardResolution("/templates/simple-url.pebble");
	}

}
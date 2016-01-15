package com.hectorlopezfernandez.pebblestripes.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/request-attributes.action")
public class RequestAttributesAction extends BaseAction {

	@DefaultHandler
	public Resolution execute() {
		getContext().getRequest().setAttribute("bla", "BLA");
		return new ForwardResolution("/templates/request-attributes.pebble");
	}

}
package com.hectorlopezfernandez.pebblestripes.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/simple-url.action")
public class SimpleUrlAction extends BaseAction {

	@DefaultHandler
	public Resolution execute() {
		return new ForwardResolution("/templates/simple-url.pebble");
	}

}
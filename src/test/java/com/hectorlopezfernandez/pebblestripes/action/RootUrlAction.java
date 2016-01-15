package com.hectorlopezfernandez.pebblestripes.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/root-url.action")
public class RootUrlAction extends BaseAction {

	@DefaultHandler
	public Resolution execute() {
		return new ForwardResolution("/templates/root-url.pebble");
	}

}
package com.hectorlopezfernandez.pebblestripes.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/index.action")
public class IndexAction extends BaseAction {

	@DefaultHandler
	public Resolution execute() {
		return new StreamingResolution("text/plain", "index!");
	}

}
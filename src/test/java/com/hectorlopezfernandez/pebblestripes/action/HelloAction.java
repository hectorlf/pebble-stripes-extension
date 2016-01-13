package com.hectorlopezfernandez.pebblestripes.action;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

@UrlBinding("/hello/{name}/msg.action")
public class HelloAction extends BaseAction {

	private String name;

	@DefaultHandler
	public Resolution execute() {
		return new ForwardResolution("/templates/hello.pebble");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
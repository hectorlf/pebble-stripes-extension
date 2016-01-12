package com.hectorlopezfernandez.pebblestripes;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import net.sourceforge.stripes.controller.DispatcherServlet;
import net.sourceforge.stripes.controller.StripesFilter;

@RunWith(JUnit4.class)
public class StripesExtensionTests {

	private static Server server;
	private static int port;
	private static HttpClient httpClient;

	@BeforeClass
	public static void setup() {
		Server server = new Server(0);
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		context.setContextPath("/test");
		FilterHolder sfh = context.addFilter(StripesFilter.class, "*.action", EnumSet.of(DispatcherType.FORWARD,DispatcherType.INCLUDE,DispatcherType.REQUEST));
		sfh.setInitParameter("ActionResolver.Packages", "com.hectorlopezfernandez.pebblestripes");
		context.addServlet(DispatcherServlet.class, "*.action");
		server.setHandler(context);
		try {
			server.start();
		} catch (Exception e) {
			throw new RuntimeException("Unable to start jetty: " + e.getMessage());
		}
		port = ((ServerConnector)server.getConnectors()[0]).getLocalPort();
		httpClient = new HttpClient();
		try {
			httpClient.start();
		} catch (Exception e) {
			throw new RuntimeException("Unable to start jetty client: " + e.getMessage());
		}
	}
	
	@AfterClass
	public static void tearDown() {
		try {
			httpClient.stop();
		} catch (Exception e1) {
			// life is hard
		}
		try {
			server.stop();
		} catch (Exception e) {
			// life is hard
		}
	}

	@Test
	public void testServerUp() throws Exception {
		ContentResponse response = httpClient.newRequest("localhost", port).method(HttpMethod.GET).path("/test/index.action").send();
		Assert.assertEquals(200, response.getStatus());
		System.out.println(response.getStatus() + " - " + response.getContentAsString());
	}

}
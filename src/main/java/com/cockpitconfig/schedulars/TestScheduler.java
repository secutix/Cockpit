package com.cockpitconfig.schedulars;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

public class TestScheduler {

	public static void main(String args[]) throws Exception {
		// create CamelContext
		CamelContext context = new DefaultCamelContext();
		context.disableJMX();
		// add our route to the CamelContext
		context.addRoutes(new RouteBuilder() {
			@Override
			public void configure() {
				from("timer://myTimer?period=2000").setBody()
						.simple("Current time is ${header.firedTime}")
						.to("stream:out");
			}
		});

		context.start();
		Thread.sleep(10000);

		context.stop();
	}
}
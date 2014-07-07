package org.teknux.dropbitz.test.controllers;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.junit.ClassRule;


public class BaseControllerTest {

	@ClassRule
	public static final StartApplicationRule RULE = new StartApplicationRule();

	public String getBaseUrl() {
		return String.format("http://localhost:%d", RULE.getConfiguration().getPort());
	}

	public Client createClient() {
		return ClientBuilder.newClient();
	}

	public WebTarget getWebTarget(final String relativeUrl) {
		return createClient().target(getBaseUrl() + relativeUrl);
	}
}

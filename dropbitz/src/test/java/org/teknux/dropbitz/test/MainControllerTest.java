package org.teknux.dropbitz.test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Assert;
import org.junit.Test;


public class MainControllerTest extends BaseControllerTest {

	@Test
	public void testIndex() {
		final Response r = getWebTarget("/").request(MediaType.TEXT_HTML_TYPE).get();

		Assert.assertEquals(Status.OK.getStatusCode(), r.getStatus());
	}

	@Test
	public void testAuth() {
		Form form = new Form();
		form.param("secureId", "abcd");

		final Response r = getWebTarget("/authenticate").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		// TODO should be Status.SEE_OTHER
		Assert.assertEquals(Status.OK.getStatusCode(), r.getStatus());
	}

	@Test
	public void testNotFound() {
		final Response r = getWebTarget("/not/found").request().get();
		Assert.assertEquals(Status.NOT_FOUND.getStatusCode(), r.getStatus());
	}
}

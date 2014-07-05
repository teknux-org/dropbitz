package org.teknux.dropbitz.test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Assert;


public class MainControllerTest extends BaseControllerTest {

	public void testIndex() {
		final Response r = getWebTarget("/").request(MediaType.TEXT_HTML_TYPE).get();

		Assert.assertEquals(Status.OK.getStatusCode(), r.getStatus());
	}

	public void testAuth() {
		Form form = new Form();
		form.param("secureId", "bigup is awesome");

		final Response r = getWebTarget("/authenticate").request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED_TYPE));
		Assert.assertEquals(Status.SEE_OTHER.getStatusCode(), r.getStatus());
	}

	public void testNotFound() {
		final Response r = getWebTarget("/not/found").request().get();
		Assert.assertEquals(Status.NOT_FOUND.getStatusCode(), r.getStatus());
	}
}

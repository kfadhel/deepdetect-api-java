package com.deepdetect.api.request;

import static com.deepdetect.api.TestUtils.DELETE_SERVICE_RESPONSE_FILE;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.deepdetect.api.TestUtils;
import com.deepdetect.api.exceptions.DeepDetectException;
import com.deepdetect.api.response.DeleteServiceResponse;

import okhttp3.mockwebserver.MockResponse;

public class DeleteServiceRequestTest extends AbstractRequestTest {

	private static final String SERVICE_NAME = "myserv";

	@Override
	public void testRequestRequiresBaseURL() throws DeepDetectException {
		DeleteServiceRequest.newDeleteServiceRequest().build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDeleteServiceRequestRequiredServiceName() {
		DeleteServiceRequest.newDeleteServiceRequest() //
				.baseURL(baseUrl) //
				.build();
	}

	@Test
	public void testDeleteServiceRequestReturnsExpectedResult()
			throws DeepDetectException, IOException, InterruptedException {

		server.enqueue(new MockResponse().setBody(TestUtils.getResourceAsString(DELETE_SERVICE_RESPONSE_FILE)));

		DeleteServiceResponse response = DeleteServiceRequest.newDeleteServiceRequest() //
				.baseURL(baseUrl) //
				.service(SERVICE_NAME) //
				.build().process();

		assertThat(response.getStatus().getCode(), is(200));
		assertThat(response.getStatus().getMessage(), is("OK"));
		assertThat(server.takeRequest().getPath(), is("/services/" + SERVICE_NAME));
	}
}

package com.deepdetect.api.request;

import static com.deepdetect.api.TestUtils.GET_INFO_SERVICE_RESPONSE_MULTIPLE_JOBS_FILE;
import static com.deepdetect.api.TestUtils.GET_INFO_SERVICE_RESPONSE_SINGLE_JOB_FILE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.deepdetect.api.TestUtils;
import com.deepdetect.api.exceptions.DeepDetectException;
import com.deepdetect.api.response.InfoServiceResponse;

import okhttp3.mockwebserver.MockResponse;

public class InfoServiceRequestTest extends AbstractRequestTest {

	private static final String SERVICE = "myserv";

	@Override
	public void testRequestRequiresBaseURL() throws DeepDetectException {
		InfoServiceRequest.newInfoServiceRequest().build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRequestRequiresServiceName() throws DeepDetectException {
		InfoServiceRequest.newInfoServiceRequest() //
				.baseURL(baseUrl) //
				.build();
	}

	@Test
	public void testReturnExpectedResultForSingleJob() throws DeepDetectException, IOException, InterruptedException {
		server.enqueue(new MockResponse().setBody(TestUtils.getResourceAsString(GET_INFO_SERVICE_RESPONSE_SINGLE_JOB_FILE)));

		InfoServiceResponse response = InfoServiceRequest.newInfoServiceRequest() //
				.baseURL(baseUrl) //
				.service(SERVICE) //
				.build().process();

		assertThat(response.getStatus().getCode(), is(200));
		assertThat(response.getStatus().getMessage(), is("OK"));
		assertThat(response.getBody().getJobs().size(), is(1));
		assertThat(server.takeRequest().getPath(), is("/services/myserv"));
	}
	
	@Test
	public void testReturnExpectedResultForMultipleJobs() throws DeepDetectException, IOException, InterruptedException {
		server.enqueue(new MockResponse().setBody(TestUtils.getResourceAsString(GET_INFO_SERVICE_RESPONSE_MULTIPLE_JOBS_FILE)));

		InfoServiceResponse response = InfoServiceRequest.newInfoServiceRequest() //
				.baseURL(baseUrl) //
				.service(SERVICE) //
				.build().process();

		assertThat(response.getStatus().getCode(), is(200));
		assertThat(response.getStatus().getMessage(), is("OK"));
		assertThat(response.getBody().getJobs().size(), is(2));
		assertThat(server.takeRequest().getPath(), is("/services/myserv"));
	}
}

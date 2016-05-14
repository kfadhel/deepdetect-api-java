package com.deepdetect.api.request;

import static com.deepdetect.api.TestUtils.GET_INFO_RESPONSE_FILE_PATH;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.deepdetect.api.TestUtils;
import com.deepdetect.api.exceptions.DeepDetectException;
import com.deepdetect.api.response.InfoResponse;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

public class InfoRequestTest {

	private MockWebServer server;
	private String baseUrl;

	@Before
	public void setUp() throws IOException {
		server = new MockWebServer();
		server.start();
		baseUrl = server.url("/").toString();
	}

	@Test
	public void testInfoRequestReturnsExpectedResult() throws DeepDetectException, IOException {

		server.enqueue(new MockResponse().setBody(TestUtils.getResourceAsString(GET_INFO_RESPONSE_FILE_PATH)));
		InfoResponse response = InfoRequest.newInfoRequest() //
				.baseURL(baseUrl) //
				.build().process();

		assertThat(response.getStatus().getCode(), is(200));
		assertThat(response.getStatus().getMessage(), is("OK"));
		assertThat(response.getHead().getMethod(), is("/info"));

	}

	@Test(expected = IllegalArgumentException.class)
	public void testInfoRequestRequiresBaseURL() throws DeepDetectException {
		InfoRequest.newInfoRequest() //
				.build().process();
	}

	@After
	public void tearDown() throws IOException {
		server.shutdown();
	}
}

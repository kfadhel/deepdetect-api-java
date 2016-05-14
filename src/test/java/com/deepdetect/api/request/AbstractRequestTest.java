package com.deepdetect.api.request;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.deepdetect.api.exceptions.DeepDetectException;

import okhttp3.mockwebserver.MockWebServer;

public abstract class AbstractRequestTest {

	protected MockWebServer server;
	protected String baseUrl;

	@Before
	public void setUp() throws IOException {
		server = new MockWebServer();
		server.start();
		baseUrl = server.url("/").toString();
	}

	@Test(expected = IllegalArgumentException.class)
	public abstract void testRequestRequiresBaseURL() throws DeepDetectException;

	@After
	public void tearDown() throws IOException {
		server.shutdown();
	}
}

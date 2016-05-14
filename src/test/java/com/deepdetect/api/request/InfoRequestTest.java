package com.deepdetect.api.request;

import static com.deepdetect.api.TestUtils.GET_INFO_RESPONSE_FILE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.Test;

import com.deepdetect.api.TestUtils;
import com.deepdetect.api.exceptions.DeepDetectException;
import com.deepdetect.api.response.InfoResponse;

import okhttp3.mockwebserver.MockResponse;

public class InfoRequestTest extends AbstractRequestTest {

	@Test
	public void testInfoRequestReturnsExpectedResult() throws DeepDetectException, IOException {

		server.enqueue(new MockResponse().setBody(TestUtils.getResourceAsString(GET_INFO_RESPONSE_FILE)));
		InfoResponse response = InfoRequest.newInfoRequest() //
				.baseURL(baseUrl) //
				.build().process();

		assertThat(response.getStatus().getCode(), is(200));
		assertThat(response.getStatus().getMessage(), is("OK"));
		assertThat(response.getHead().getMethod(), is("/info"));

	}

	@Override
	public void testRequestRequiresBaseURL() throws DeepDetectException {
		InfoRequest.newInfoRequest() //
				.build().process();
	}
}

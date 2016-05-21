package com.deepdetect.api.request;

import static com.deepdetect.api.TestUtils.PREDICT_RESPONSE_FILE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.deepdetect.api.TestUtils;
import com.deepdetect.api.data.PredictionClass;
import com.deepdetect.api.exceptions.DeepDetectException;
import com.deepdetect.api.response.PredictResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

public class PredictRequestTest extends AbstractRequestTest {

	private static final String SERVICE_NAME = "imageserv";
	private static final String DATA = "http://i.ytimg.com/vi/0vxOhd4qlnA/maxresdefault.jpg";
	private JsonObject params;

	@Before
	public void internalSetUp() {
		JsonParser jsonParser = new JsonParser();
		params = jsonParser.parse("{\"input\":{\"width\":224,\"height\":224},\"output\":{\"best\":3}}") //
				.getAsJsonObject();
	}

	@Override
	public void testRequestRequiresBaseURL() throws DeepDetectException {
		PredictRequest.newPredictRequest() //
				.service(SERVICE_NAME) //
				.data(DATA) //
				.parameters(params) //
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPredictRequestRequiresServiceName() throws DeepDetectException {
		PredictRequest.newPredictRequest() //
				.baseURL(baseUrl) //
				.data(DATA) //
				.parameters(params) //
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPredictRequestRequiresInputData() throws DeepDetectException {
		PredictRequest.newPredictRequest() //
				.baseURL(baseUrl) //
				.service(SERVICE_NAME) //
				.parameters(params) //
				.build();
	}

	@Test
	public void testPredictRequestReturnsExpectedResult()
			throws DeepDetectException, IOException, InterruptedException {

		server.enqueue(new MockResponse().setBody(TestUtils.getResourceAsString(PREDICT_RESPONSE_FILE)));

		PredictResponse response = PredictRequest.newPredictRequest() //
				.baseURL(baseUrl) //
				.service(SERVICE_NAME) //
				.data(DATA) //
				.parameters(params) //
				.build().process();

		RecordedRequest request = server.takeRequest(2, SECONDS);
		assertThat(request.getMethod(), is("POST"));
		assertThat(request.getPath(), is("/predict"));

		String expectedBody = "{\"service\":\"imageserv\",\"parameters\":{\"input\":{\"width\":224,\"height\":224},\"output\":{\"best\":3}},\"data\":[\"http://i.ytimg.com/vi/0vxOhd4qlnA/maxresdefault.jpg\"]}";
		assertThat(request.getBody().readUtf8(), is(expectedBody));

		assertThat(response.getStatus().getCode(), is(200));
		assertThat(response.getHead().getService(), is(SERVICE_NAME));
		assertThat(response.getBody().getPredictions().size(), is(1));
		assertThat(response.getBody().getPredictions().get(0).getUri(), is(DATA));
		assertThat(response.getBody().getPredictions().get(0).getLoss(), is(0.0));

		List<PredictionClass> classes = response.getBody().getPredictions().get(0).getClasses();
		assertThat(classes.size(), is(3));
		assertThat(classes.get(1).getCategory(), is("n03127747 crash helmet"));
		assertThat(classes.get(1).getProbability(), is(0.20703653991222382));
	}
}

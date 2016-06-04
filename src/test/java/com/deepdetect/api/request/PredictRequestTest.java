package com.deepdetect.api.request;

import static com.deepdetect.api.TestUtils.PREDICT_RESPONSE_FILE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

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
	private JsonObject input, output, mllibParams;
	private String inputString, outputString, mllibParamsString;

	@Before
	public void internalSetUp() {

		inputString = "{\"width\":224,\"height\":224}";
		outputString = "{\"best\":3}";
		mllibParamsString = "{\"gpu\":true}";

		JsonParser jsonParser = new JsonParser();
		input = jsonParser.parse(inputString).getAsJsonObject();
		output = jsonParser.parse(outputString).getAsJsonObject();
		mllibParams = jsonParser.parse(mllibParamsString).getAsJsonObject();

	}

	@Override
	public void testRequestRequiresBaseURL() throws DeepDetectException {
		PredictRequest.newPredictRequest() //
				.service(SERVICE_NAME) //
				.data(DATA) //
				.input(input) //
				.output(output) //
				.mllibParams(mllibParams) //
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPredictRequestRequiresServiceName() throws DeepDetectException {
		PredictRequest.newPredictRequest() //
				.baseURL(baseUrl) //
				.data(DATA) //
				.input(input) //
				.output(output) //
				.mllibParams(mllibParams) //
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testPredictRequestRequiresInputData() throws DeepDetectException {
		PredictRequest.newPredictRequest() //
				.baseURL(baseUrl) //
				.service(SERVICE_NAME) //
				.input(input) //
				.output(output) //
				.mllibParams(mllibParams) //
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
				.input(input) //
				.output(output) //
				.mllibParams(mllibParams) //
				.build().process();

		RecordedRequest request = server.takeRequest(2, SECONDS);

		validate(request, response);
	}

	@Test
	public void testPredictRequestReturnsExpectedResultWhenUsingStringParams()
			throws DeepDetectException, IOException, InterruptedException {

		server.enqueue(new MockResponse().setBody(TestUtils.getResourceAsString(PREDICT_RESPONSE_FILE)));

		PredictResponse response = PredictRequest.newPredictRequest() //
				.baseURL(baseUrl) //
				.service(SERVICE_NAME) //
				.data(DATA) //
				.input(input) //
				.output(output) //
				.mllibParams(mllibParams) //
				.build().process();

		RecordedRequest request = server.takeRequest(2, SECONDS);

		validate(request, response);
	}

	private void validate(RecordedRequest request, PredictResponse response) {
		assertThat(request.getMethod(), is("POST"));
		assertThat(request.getPath(), is("/predict"));

		String expectedBody = "{\"service\":\"imageserv\",\"parameters\":{\"input\":{\"width\":224,\"height\":224},\"output\":{\"best\":3},\"mllib\":{\"gpu\":true}},\"data\":[\"http://i.ytimg.com/vi/0vxOhd4qlnA/maxresdefault.jpg\"]}";
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

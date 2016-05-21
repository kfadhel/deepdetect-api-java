package com.deepdetect.api.request;

import static com.deepdetect.api.TestUtils.TRAIN_JOB_RESPONSE_FILE;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.deepdetect.api.TestUtils;
import com.deepdetect.api.exceptions.DeepDetectException;
import com.deepdetect.api.response.TrainJobResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

public class TrainJobRequestTest extends AbstractRequestTest {

	private static final String SERVICE_NAME = "myserv";
	private static final String DATA = "/home/me/example/train.csv";
	private JsonObject parameters;

	@Before
	public void internalSetUp() {
		JsonParser jsonParser = new JsonParser();
		parameters = jsonParser.parse(
				"{\"mllib\":{\"gpu\":true,\"solver\":{\"iterations\":300,\"test_interval\":100},\"net\":{\"batch_size\":5000}},\"input\":{\"label\":\"target\",\"id\":\"id\",\"separator\":\",\",\"shuffle\":true,\"test_split\":0.15,\"scale\":true},\"output\":{\"measure\":[\"acc\",\"mcll\"]}}") //
				.getAsJsonObject();

	}

	@Override
	public void testRequestRequiresBaseURL() throws DeepDetectException {
		TrainJobRequest.newTrainJobRequest()//
				.async(true) //
				.parameters(parameters) //
				.data(DATA) //
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTrainJobRequestRequiresServiceName() {
		TrainJobRequest.newTrainJobRequest() //
				.baseURL(baseUrl) //
				.async(true) //
				.parameters(parameters) //
				.data(DATA) //
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTrainJobRequestRequiresAsyncType() {
		TrainJobRequest.newTrainJobRequest() //
				.baseURL(baseUrl) //
				.service(SERVICE_NAME) //
				.parameters(parameters) //
				.data(DATA) //
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testTrainJobRequestRequiresJsonParameters() {
		TrainJobRequest.newTrainJobRequest() //
				.baseURL(baseUrl) //
				.service(SERVICE_NAME) //
				.parameters(parameters) //
				.data(DATA) //
				.build();
	}

	@Test
	public void testTrainJobRequestReturnsExpectedResult()
			throws DeepDetectException, IOException, InterruptedException {

		server.enqueue(new MockResponse().setBody(TestUtils.getResourceAsString(TRAIN_JOB_RESPONSE_FILE)));

		TrainJobResponse response = TrainJobRequest.newTrainJobRequest() //
				.baseURL(baseUrl) //
				.service(SERVICE_NAME) //
				.async(false) //
				.parameters(parameters) //
				.data(DATA) //
				.build().process();

		String expectedRequestBody = "{\"service\":\"myserv\",\"async\":false,\"parameters\":{\"mllib\":{\"gpu\":true,\"solver\":{\"iterations\":300,\"test_interval\":100},\"net\":{\"batch_size\":5000}},\"input\":{\"label\":\"target\",\"id\":\"id\",\"separator\":\",\",\"shuffle\":true,\"test_split\":0.15,\"scale\":true},\"output\":{\"measure\":[\"acc\",\"mcll\"]}},\"data\":[\"/home/me/example/train.csv\"]}";

		RecordedRequest request = server.takeRequest(2, TimeUnit.SECONDS);

		assertThat(request.getPath(), is("/train"));
		assertThat(request.getMethod(), is("PUT"));
		assertThat(request.getBody().readUtf8(), is(expectedRequestBody));

		assertThat(response.getStatus().getCode(), is(201));
		assertThat(response.getStatus().getMessage(), is("Created"));
		assertThat(response.getHead().getMethod(), is("/train"));

	}
}

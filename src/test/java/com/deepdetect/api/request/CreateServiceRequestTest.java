package com.deepdetect.api.request;

import static com.deepdetect.api.TestUtils.CREATE_SERVICE_RESPONSE_FILE;
import static com.deepdetect.api.TestUtils.getResourceAsString;
import static com.deepdetect.api.enums.MLType.SUPERVISED;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import com.deepdetect.api.enums.MLType;
import com.deepdetect.api.exceptions.DeepDetectException;
import com.deepdetect.api.response.CreateServiceResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

public class CreateServiceRequestTest extends AbstractRequestTest {

	private static final String DESCRIPTION = "example classification service";
	private static final String ML_LIB = "caffe";
	private static final String SERVICE_NAME = "myserv";
	private static final MLType ML_TYPE = SUPERVISED;

	private JsonObject model, input, mllibParams;

	private String modelString, inputString, mllibParamsString;

	@Before
	public void internalSetUp() {

		inputString = "{\"connector\":\"csv\"}";
		mllibParamsString = "{\"template\":\"mlp\",\"nclasses\":9,\"activation\":\"prelu\",\"layers\":[512,512,512]}";
		modelString = "{\"repository\":\"/home/me/models/example\"}";

		model = new JsonObject();
		model.addProperty("repository", "/home/me/models/example");

		input = new JsonObject();
		input.addProperty("connector", "csv");

		mllibParams = new JsonObject();
		mllibParams.addProperty("template", "mlp");
		mllibParams.addProperty("nclasses", 9);
		mllibParams.addProperty("activation", "prelu");

		JsonArray array = new JsonArray();
		array.add(512);
		array.add(512);
		array.add(512);

		mllibParams.add("layers", array);
	}

	@Override
	public void testRequestRequiresBaseURL() throws DeepDetectException {
		CreateServiceRequest.newCreateServiceRequest() //
				.build().process();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateServiceRequestRequiresServiceName() throws DeepDetectException {
		CreateServiceRequest.newCreateServiceRequest() //
				.baseURL(baseUrl) //
				.description(DESCRIPTION) //
				.mllib(ML_LIB) //
				.type(ML_TYPE) //
				.input(input) //
				.model(model) //
				.mllibParams(mllibParams) //
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateServiceRequestRequiresModel() throws DeepDetectException {
		CreateServiceRequest.newCreateServiceRequest() //
				.baseURL(baseUrl) //
				.name(SERVICE_NAME) //
				.description(DESCRIPTION) //
				.mllib(ML_LIB) //
				.type(ML_TYPE) //
				.input(input) //
				.mllibParams(mllibParams) //
				.build();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateServiceRequestRequiresInput() throws DeepDetectException {
		CreateServiceRequest.newCreateServiceRequest() //
				.baseURL(baseUrl) //
				.name(SERVICE_NAME) //
				.description(DESCRIPTION) //
				.mllib(ML_LIB) //
				.type(ML_TYPE) //
				.model(model) //
				.mllibParams(mllibParams) //
				.build();
	}

	@Test
	public void testCreateServiceRequestReturnsExpectedResult()
			throws DeepDetectException, IOException, InterruptedException {
		server.enqueue(new MockResponse().setBody(getResourceAsString(CREATE_SERVICE_RESPONSE_FILE)));

		CreateServiceResponse response = CreateServiceRequest.newCreateServiceRequest() //
				.baseURL(baseUrl) //
				.name(SERVICE_NAME) //
				.description(DESCRIPTION) //
				.mllib(ML_LIB) //
				.type(ML_TYPE) //
				.input(input) //
				.model(model) //
				.mllibParams(mllibParams) //
				.build().process();

		RecordedRequest request = server.takeRequest(2, TimeUnit.SECONDS);

		validate(request, response);

	}

	@Test
	public void testCreateServiceRequestReturnsExpectedResultWhenUsingStringParams()
			throws DeepDetectException, IOException, InterruptedException {
		server.enqueue(new MockResponse().setBody(getResourceAsString(CREATE_SERVICE_RESPONSE_FILE)));

		CreateServiceResponse response = CreateServiceRequest.newCreateServiceRequest() //
				.baseURL(baseUrl) //
				.name(SERVICE_NAME) //
				.description(DESCRIPTION) //
				.mllib(ML_LIB) //
				.type(ML_TYPE) //
				.input(inputString) //
				.model(modelString) //
				.mllibParams(mllibParamsString) //
				.build().process();

		RecordedRequest request = server.takeRequest(2, TimeUnit.SECONDS);

		validate(request, response);

	}

	private void validate(RecordedRequest request, CreateServiceResponse response) {

		String expectedRequestBody = "{\"mllib\":\"caffe\",\"description\":\"example classification service\",\"type\":\"supervised\",\"parameters\":{\"input\":{\"connector\":\"csv\"},\"mllib\":{\"template\":\"mlp\",\"nclasses\":9,\"activation\":\"prelu\",\"layers\":[512,512,512]}},\"model\":{\"repository\":\"/home/me/models/example\"}}";

		assertThat(request.getPath(), is("/services/" + SERVICE_NAME));
		assertThat(request.getMethod(), is("PUT"));
		assertThat(request.getBody().readUtf8(), is(expectedRequestBody));

		assertThat(response.getStatus().getCode(), is(201));
		assertThat(response.getStatus().getMessage(), is("Created"));
	}

}

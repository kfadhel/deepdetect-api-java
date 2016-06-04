package com.deepdetect.api.request;

import static com.deepdetect.api.enums.MLType.SUPERVISED;
import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;

import com.deepdetect.api.enums.MLType;
import com.deepdetect.api.enums.Operation;
import com.deepdetect.api.response.CreateServiceResponse;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CreateServiceRequest extends DeepDetectRequest<CreateServiceResponse> {

	private JsonObject jsonContent;
	private String serviceName;

	// Suppresses default constructor, ensuring non-instantiability.
	private CreateServiceRequest() {
	}

	/**
	 * create a new request for create service api
	 * 
	 * @return a builder instance
	 */
	public static CreateServiceRequestBuilder newCreateServiceRequest() {
		return new CreateServiceRequestBuilder();
	}

	public static class CreateServiceRequestBuilder {
		private String name, url, mllib, descr;
		private MLType mltype = SUPERVISED;

		private JsonObject model, input, mllibParams;

		private JsonParser jsonParser = new JsonParser();

		public CreateServiceRequestBuilder name(String name) {
			this.name = name;
			return this;
		}

		public CreateServiceRequestBuilder baseURL(String url) {
			this.url = url;
			return this;
		}

		public CreateServiceRequestBuilder mllib(String lib) {
			this.mllib = lib;
			return this;
		}

		public CreateServiceRequestBuilder description(String dscr) {
			this.descr = dscr;
			return this;
		}

		public CreateServiceRequestBuilder type(MLType type) {
			this.mltype = type;
			return this;
		}

		public CreateServiceRequestBuilder model(JsonObject model) {
			this.model = model;
			return this;
		}

		public CreateServiceRequestBuilder model(String modelString) {
			this.model = jsonParser.parse(modelString).getAsJsonObject();
			return this;
		}

		public CreateServiceRequestBuilder input(JsonObject input) {
			this.input = input;
			return this;
		}

		public CreateServiceRequestBuilder input(String inputString) {
			this.input = jsonParser.parse(inputString).getAsJsonObject();
			return this;
		}

		public CreateServiceRequestBuilder mllibParams(JsonObject mllibParams) {
			this.mllibParams = mllibParams;
			return this;
		}

		public CreateServiceRequestBuilder mllibParams(String mllibParamsString) {
			this.mllibParams = jsonParser.parse(mllibParamsString).getAsJsonObject();
			return this;
		}

		/**
		 * process the builder and create an instance of the create service
		 * request
		 * 
		 * @return DeleteTrainingJobRequest
		 */
		public CreateServiceRequest build() {

			checkArgument(!Strings.isNullOrEmpty(url), "url is required");
			checkArgument(!Strings.isNullOrEmpty(name), "service name is required");
			checkArgument(mltype != null, "type is required");
			checkArgument(model != null, "model is required");
			checkArgument(input != null, "input is required");

			CreateServiceRequest request = new CreateServiceRequest();

			request.baseURL = url;
			request.serviceName = name;

			JsonObject json = new JsonObject();
			json.addProperty("mllib", mllib);
			json.addProperty("description", descr);
			json.addProperty("type", mltype.getValue());

			JsonObject paramsJson = new JsonObject();
			paramsJson.add("input", input);
			if (mllibParams != null)
				paramsJson.add("mllib", mllibParams);

			json.add("parameters", paramsJson);

			json.add("model", model);

			request.jsonContent = json;

			return request;
		}
	}

	@Override
	protected Operation getOperation() {
		return Operation.SERVICES;
	}

	@Override
	protected CreateServiceResponse internalProcess() {
		return new Gson().fromJson(doPut(), CreateServiceResponse.class);
	}

	@Override
	protected JsonObject getContent() {
		return jsonContent;
	}

	@Override
	protected String getPath() {
		return getOperation().getValue() + "/" + serviceName;
	}

	@Override
	protected Map<String, String> getRequestParams() {
		return new HashMap<String, String>();
	}

}

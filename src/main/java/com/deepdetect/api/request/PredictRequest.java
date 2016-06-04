package com.deepdetect.api.request;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;

import com.deepdetect.api.enums.Operation;
import com.deepdetect.api.response.PredictResponse;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PredictRequest extends DeepDetectRequest<PredictResponse> {

	private JsonObject data;

	// Suppresses default constructor, ensuring non-instantiability.
	private PredictRequest() {
	}

	/**
	 * create a new request for predict api
	 * 
	 * @return a builder instance
	 */
	public static PredictBuilder newPredictRequest() {
		return new PredictBuilder();
	}

	public static class PredictBuilder {
		private String url, service;
		private JsonArray data;
		private JsonObject input, output, mllibParams;

		private JsonParser jsonParser = new JsonParser();

		public PredictBuilder baseURL(String url) {
			this.url = url;
			return this;
		}

		public PredictBuilder service(String service) {
			this.service = service;
			return this;
		}

		public PredictBuilder mllibParams(JsonObject mllibParams) {
			this.mllibParams = mllibParams;
			return this;
		}

		public PredictBuilder mllibParams(String mllibParamsString) {
			this.mllibParams = jsonParser.parse(mllibParamsString).getAsJsonObject();
			return this;
		}

		public PredictBuilder input(JsonObject input) {
			this.input = input;
			return this;
		}

		public PredictBuilder input(String inputString) {
			this.input = jsonParser.parse(inputString).getAsJsonObject();
			return this;
		}

		public PredictBuilder output(JsonObject output) {
			this.output = output;
			return this;
		}

		public PredictBuilder output(String outputString) {
			this.output = jsonParser.parse(outputString).getAsJsonObject();
			return this;
		}

		public PredictBuilder data(String... dataArray) {
			JsonArray data = new JsonArray();
			for (String str : dataArray) {
				data.add(str);
			}

			this.data = data;
			return this;
		}

		/**
		 * process the builder and create an instance of the predict request
		 * 
		 * @return PredictRequest
		 */
		public PredictRequest build() {

			checkArgument(!Strings.isNullOrEmpty(url), "url is required");
			checkArgument(!Strings.isNullOrEmpty(service), "service name is required");
			checkArgument(data != null, "data is required");

			PredictRequest request = new PredictRequest();
			request.baseURL = url;
			JsonObject requestData = new JsonObject();
			requestData.addProperty("service", service);

			JsonObject paramsObj = new JsonObject();
			if (input != null)
				paramsObj.add("input", input);
			if (output != null)
				paramsObj.add("output", output);
			if (mllibParams != null)
				paramsObj.add("mllib", mllibParams);

			if (!paramsObj.isJsonNull()) {
				requestData.add("parameters", paramsObj);
			}

			requestData.add("data", data);

			request.data = requestData;
			return request;
		}
	}

	@Override
	protected JsonObject getContent() {
		return data;
	}

	@Override
	protected String getPath() {
		return getOperation().getValue();
	}

	@Override
	protected Operation getOperation() {
		return Operation.PREDICT;
	}

	@Override
	protected PredictResponse internalProcess() {

		String stringResponse = doPost();
		// create a jsonElement from String
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonResponse = jsonParser.parse(stringResponse);

		JsonElement predictions = jsonResponse.getAsJsonObject().get("body").getAsJsonObject().get("predictions");

		// check if predictions is an array or an object:
		// if it contains a single element, the server returns it as an object
		// and then convert it to an array with single element

		JsonArray jobsArray = new JsonArray();
		if (predictions.isJsonObject()) {
			jobsArray.add(predictions);
		} else {
			jobsArray = predictions.getAsJsonArray();
		}

		for (JsonElement elmnt : jobsArray) {
			if (elmnt.getAsJsonObject().get("classes").isJsonObject()) {
				JsonArray classesArray = new JsonArray();
				classesArray.add(elmnt.getAsJsonObject().get("classes"));
				elmnt.getAsJsonObject().add("classes", classesArray);
			}
		}

		jsonResponse.getAsJsonObject().get("body").getAsJsonObject().add("predictions", jobsArray);

		return new Gson().fromJson(jsonResponse, PredictResponse.class);
	}

	@Override
	protected Map<String, String> getRequestParams() {
		return new HashMap<String, String>();
	}

}

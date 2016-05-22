package com.deepdetect.api.request;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;

import com.deepdetect.api.enums.Operation;
import com.deepdetect.api.response.TrainJobResponse;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class TrainJobRequest extends DeepDetectRequest<TrainJobResponse> {

	private JsonObject jsonContent;

	// Suppresses default constructor, ensuring non-instantiability.
	private TrainJobRequest() {
	}

	/**
	 * create a new request for train job api
	 * 
	 * @return a builder instance
	 */
	public static TrainJobBuilder newTrainJobRequest() {
		return new TrainJobBuilder();
	}

	public static class TrainJobBuilder {
		private String url, service;
		private boolean async;
		private JsonArray data;

		private JsonObject mllibParams, input, output;

		public TrainJobBuilder baseURL(String url) {
			this.url = url;
			return this;
		}

		public TrainJobBuilder service(String service) {
			this.service = service;
			return this;
		}

		public TrainJobBuilder async(boolean async) {
			this.async = async;
			return this;
		}

		public TrainJobBuilder mllibParams(JsonObject mllibParams) {
			this.mllibParams = mllibParams;
			return this;
		}

		public TrainJobBuilder input(JsonObject input) {
			this.input = input;
			return this;
		}

		public TrainJobBuilder output(JsonObject output) {
			this.output = output;
			return this;
		}

		public TrainJobBuilder data(String... dataArray) {
			JsonArray array = new JsonArray();
			for (String str : dataArray)
				array.add(str);
			this.data = array;
			return this;
		}

		/**
		 * process the builder and create an instance of the train job request
		 * 
		 * @return PredictRequest
		 */
		public TrainJobRequest build() {

			checkArgument(!Strings.isNullOrEmpty(url), "url is required");
			checkArgument(!Strings.isNullOrEmpty(service), "service name is required");
			checkArgument(mllibParams != null, "mllibParams is required");
			checkArgument(input != null, "input is required");
			checkArgument(output != null, "output is required");

			TrainJobRequest request = new TrainJobRequest();
			request.baseURL = url;

			JsonObject content = new JsonObject();
			content.addProperty("service", service);
			content.addProperty("async", async);

			JsonObject paramsJson = new JsonObject();
			paramsJson.add("mllib", mllibParams);
			paramsJson.add("input", input);
			paramsJson.add("output", output);

			content.add("parameters", paramsJson);

			if (data != null)
				content.add("data", data);

			request.jsonContent = content;

			return request;
		}
	}

	@Override
	protected JsonObject getContent() {

		return jsonContent;
	}

	@Override
	protected String getPath() {
		return getOperation().getValue();
	}

	@Override
	protected Operation getOperation() {
		return Operation.TRAIN;
	}

	@Override
	protected TrainJobResponse internalProcess() {
		return new Gson().fromJson(doPut(), TrainJobResponse.class);
	}

	@Override
	protected Map<String, String> getRequestParams() {
		return new HashMap<String, String>();
	}

}

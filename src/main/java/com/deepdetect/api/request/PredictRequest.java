package com.deepdetect.api.request;

import static com.google.common.base.Preconditions.checkArgument;

import com.deepdetect.api.enums.Operation;
import com.deepdetect.api.response.PredictResponse;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
		JsonObject data;

		public PredictBuilder baseURL(String url) {
			this.url = url;
			return this;
		}

		public PredictBuilder service(String service) {
			this.service = service;
			return this;
		}

		public PredictBuilder data(JsonObject data) {
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
			requestData.add("parameters", data);
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
		return new Gson().fromJson(doPost(), PredictResponse.class);
	}

}

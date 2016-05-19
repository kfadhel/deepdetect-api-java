package com.deepdetect.api.request;

import static com.google.common.base.Preconditions.checkArgument;

import com.deepdetect.api.enums.Operation;
import com.deepdetect.api.response.InfoServiceResponse;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class InfoServiceRequest extends DeepDetectRequest<InfoServiceResponse> {

	private String service;

	// Suppresses default constructor, ensuring non-instantiability.
	private InfoServiceRequest() {
	}

	/**
	 * create a new request for info service api
	 * 
	 * @return a builder instance
	 */
	public static InfoServiceBuilder newInfoServiceRequest() {
		return new InfoServiceBuilder();
	}

	public static class InfoServiceBuilder {
		private String url, service;

		public InfoServiceBuilder baseURL(String url) {
			this.url = url;
			return this;
		}

		public InfoServiceBuilder service(String serviceName) {
			this.service = serviceName;
			return this;
		}

		/**
		 * process the builder and create an instance of the info service
		 * request
		 * 
		 * @return InfoServiceRequest
		 */
		public InfoServiceRequest build() {

			checkArgument(!Strings.isNullOrEmpty(url), "url is required");
			checkArgument(!Strings.isNullOrEmpty(service), "service name is required");

			InfoServiceRequest request = new InfoServiceRequest();
			request.baseURL = url;
			request.service = service;
			return request;
		}
	}

	@Override
	protected JsonObject getContent() {
		return null;
	}

	@Override
	protected String getPath() {
		return getOperation().getValue() + "/" + service;
	}

	@Override
	protected Operation getOperation() {
		return Operation.SERVICES;
	}

	@Override
	protected InfoServiceResponse internalProcess() {
		String stringResponse = doGet();

		// create a jsonElement from String
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonResponse = jsonParser.parse(stringResponse);

		JsonElement jobs = jsonResponse.getAsJsonObject().get("body").getAsJsonObject().get("jobs");

		// check if jobs is an array or an object:
		// if it contains a single element, the server returns it as an object
		// "jobs":{"job":1,"status":"running"}
		// and then convert it to an array with single element
		if (jobs.isJsonObject()) {
			JsonArray jobsArray = new JsonArray();
			jobsArray.add(jobs);
			jsonResponse.getAsJsonObject().get("body").getAsJsonObject().add("jobs", jobsArray);
		}

		return new Gson().fromJson(jsonResponse, InfoServiceResponse.class);
	}

}

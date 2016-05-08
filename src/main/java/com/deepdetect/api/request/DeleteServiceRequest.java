package com.deepdetect.api.request;

import static com.google.common.base.Preconditions.checkArgument;

import com.deepdetect.api.enums.Operation;
import com.deepdetect.api.response.DeleteServiceResponse;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class DeleteServiceRequest extends DeepDetectRequest<DeleteServiceResponse> {

	private String service;
	private String clearServiceType;

	// Suppresses default constructor, ensuring non-instantiability.
	private DeleteServiceRequest() {
	}

	/**
	 * create a new request for delete service api
	 * 
	 * @return a builder instance
	 */
	public static DeleteServiceBuilder newDeleteServiceRequest() {
		return new DeleteServiceBuilder();
	}

	public static class DeleteServiceBuilder {
		private String url, service, clearType;

		public DeleteServiceBuilder baseURL(String url) {
			this.url = url;
			return this;
		}

		public DeleteServiceBuilder service(String serviceName) {
			this.service = serviceName;
			return this;
		}

		public DeleteServiceBuilder clearType(String clearType) {
			this.clearType = clearType;
			return this;
		}

		/**
		 * process the builder and create an instance of the delete service
		 * request
		 * 
		 * @return DeleteServiceRequest
		 */
		public DeleteServiceRequest build() {

			checkArgument(!Strings.isNullOrEmpty(url), "url is required");
			checkArgument(!Strings.isNullOrEmpty(service), "service name is required");

			DeleteServiceRequest request = new DeleteServiceRequest();
			request.baseURL = url;
			request.service = service;
			request.clearServiceType = clearType;
			return request;
		}
	}

	@Override
	protected JsonObject getContent() {
		return null;
	}

	@Override
	protected String getPath() {
		StringBuilder path = new StringBuilder(getOperation().getValue());
		path.append("/").append(service);

		if (clearServiceType != null)
			path.append("?clear=").append(clearServiceType);

		return path.toString();
	}

	@Override
	protected Operation getOperation() {
		return Operation.SERVICES;
	}

	@Override
	protected DeleteServiceResponse internalProcess() {
		return new Gson().fromJson(doDelete(), DeleteServiceResponse.class);
	}

}

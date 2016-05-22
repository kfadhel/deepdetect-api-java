package com.deepdetect.api.request;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;

import com.deepdetect.api.enums.Operation;
import com.deepdetect.api.response.DeleteTrainingJobResponse;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class DeleteTrainingJobRequest extends DeepDetectRequest<DeleteTrainingJobResponse> {

	private String service;
	private int jobId;

	// Suppresses default constructor, ensuring non-instantiability.
	private DeleteTrainingJobRequest() {
	}

	/**
	 * create a new request for train job api
	 * 
	 * @return a builder instance
	 */
	public static DeleteTrainingJobBuilder newDeleteTrainingJobRequest() {
		return new DeleteTrainingJobBuilder();
	}

	public static class DeleteTrainingJobBuilder {
		private String url, service;
		private Integer jobId;

		public DeleteTrainingJobBuilder baseURL(String url) {
			this.url = url;
			return this;
		}

		public DeleteTrainingJobBuilder service(String service) {
			this.service = service;
			return this;
		}

		public DeleteTrainingJobBuilder jobId(int id) {
			this.jobId = id;
			return this;
		}

		/**
		 * process the builder and create an instance of the train job request
		 * 
		 * @return DeleteTrainingJobRequest
		 */
		public DeleteTrainingJobRequest build() {

			checkArgument(!Strings.isNullOrEmpty(url));
			checkArgument(!Strings.isNullOrEmpty(service));
			checkArgument(jobId != null);

			DeleteTrainingJobRequest request = new DeleteTrainingJobRequest();
			request.baseURL = url;
			request.service = service;
			request.jobId = jobId;
			return request;
		}
	}

	@Override
	protected JsonObject getContent() {
		return null;
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
	protected DeleteTrainingJobResponse internalProcess() {
		return new Gson().fromJson(doDelete(), DeleteTrainingJobResponse.class);
	}

	@Override
	protected Map<String, String> getRequestParams() {
		Map<String, String> requestParams = new HashMap<String, String>();
		requestParams.put("service", service);
		requestParams.put("job", String.valueOf(jobId));
		return requestParams;
	}

}

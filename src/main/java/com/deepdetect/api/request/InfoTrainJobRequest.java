package com.deepdetect.api.request;

import static com.google.common.base.Preconditions.checkArgument;

import com.deepdetect.api.enums.Operation;
import com.deepdetect.api.response.InfoTrainJobResponse;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class InfoTrainJobRequest extends DeepDetectRequest<InfoTrainJobResponse> {

	private String service;
	private int jobId;

	@SuppressWarnings("unused")
	private boolean fullHistory;
	@SuppressWarnings("unused")
	private int timeout;

	// Suppresses default constructor, ensuring non-instantiability.
	private InfoTrainJobRequest() {
	}

	/**
	 * create a new request for info train job api
	 * 
	 * @return a builder instance
	 */
	public static InfoTrainJobBuilder newInfoTrainJobRequest() {
		return new InfoTrainJobBuilder();
	}

	public static class InfoTrainJobBuilder {
		private String url, service;
		private Integer jobId;
		private int timeout;
		private boolean history;

		public InfoTrainJobBuilder baseURL(String url) {
			this.url = url;
			return this;
		}

		public InfoTrainJobBuilder service(String service) {
			this.service = service;
			return this;
		}

		public InfoTrainJobBuilder jobId(int id) {
			this.jobId = id;
			return this;
		}

		public InfoTrainJobBuilder timeout(int timeout) {
			this.timeout = timeout;
			return this;
		}

		public InfoTrainJobBuilder fullHistory(boolean history) {
			this.history = history;
			return this;
		}

		/**
		 * process the builder and create an instance of the info train job
		 * request
		 * 
		 * @return InfoTrainJobRequest
		 */
		public InfoTrainJobRequest build() {

			checkArgument(!Strings.isNullOrEmpty(url), "url is required");
			checkArgument(!Strings.isNullOrEmpty(service), "service name is required");
			checkArgument(jobId != null, "jobId is required");

			InfoTrainJobRequest request = new InfoTrainJobRequest();
			request.baseURL = url;
			request.service = service;
			request.jobId = jobId;
			request.fullHistory = history;
			request.timeout = timeout;
			return request;
		}
	}

	@Override
	protected JsonObject getContent() {
		return null;
	}

	@Override
	protected String getPath() {
		return getOperation() + "?service=" + service + "&job=" + jobId;
	}

	@Override
	protected Operation getOperation() {
		return Operation.TRAIN;
	}

	@Override
	protected InfoTrainJobResponse internalProcess() {
		return new Gson().fromJson(doGet(), InfoTrainJobResponse.class);
	}

}

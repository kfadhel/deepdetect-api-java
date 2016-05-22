package com.deepdetect.api.request;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;

import com.deepdetect.api.enums.Operation;
import com.deepdetect.api.response.InfoResponse;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Retrieve info about the server
 */

public class InfoRequest extends DeepDetectRequest<InfoResponse> {

	// Suppresses default constructor, ensuring non-instantiability.
	private InfoRequest() {
	}

	/**
	 * create a new request for info api
	 * 
	 * @return a builder instance
	 */
	public static InfoBuilder newInfoRequest() {
		return new InfoBuilder();
	}

	public static class InfoBuilder {
		private String url;

		public InfoBuilder baseURL(String url) {
			this.url = url;
			return this;
		}

		/**
		 * process the builder and create an instance of the info request
		 * 
		 * @return InfoRequest
		 */
		public InfoRequest build() {

			checkArgument(!Strings.isNullOrEmpty(url), "url is required");

			InfoRequest request = new InfoRequest();
			request.baseURL = url;
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
		return Operation.INFO;
	}

	@Override
	protected InfoResponse internalProcess() {
		return new Gson().fromJson(doGet(), InfoResponse.class);
	}

	@Override
	protected Map<String, String> getRequestParams() {
		return new HashMap<String, String>();
	}
}

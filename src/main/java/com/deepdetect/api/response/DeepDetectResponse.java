package com.deepdetect.api.response;

import com.deepdetect.api.data.Status;
import com.google.gson.Gson;

/**
 * Base class for responses from the DeepDetect API.
 */
public abstract class DeepDetectResponse {

	private Status status;

	/**
	 * the status of the api response
	 * 
	 * @return Status
	 */
	public Status getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this).toString();
	}
}

package com.deepdetect.api.response;

import com.deepdetect.api.data.Head;
import com.deepdetect.api.data.PredictBody;

/**
 * Make predictions from data
 */
public class PredictResponse extends DeepDetectResponse {

	private Head head;
	private PredictBody body;

	public Head getHead() {
		return head;
	}

	public PredictBody getBody() {
		return body;
	}
}

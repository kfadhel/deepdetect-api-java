package com.deepdetect.api.response;

import com.deepdetect.api.data.PredictBody;
import com.deepdetect.api.data.PredictHead;

/**
 * Make predictions from data
 */
public class PredictResponse extends DeepDetectResponse {

	private PredictHead head;
	private PredictBody body;

	public PredictHead getHead() {
		return head;
	}

	public PredictBody getBody() {
		return body;
	}
}

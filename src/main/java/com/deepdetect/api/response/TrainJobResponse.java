package com.deepdetect.api.response;

import com.deepdetect.api.data.Head;
import com.deepdetect.api.data.TrainJobBody;

/**
 * Response to launching a blocking or asynchronous training job from a service
 */
public class TrainJobResponse extends DeepDetectResponse {

	private Head head;
	private TrainJobBody body;

	public Head getHead() {
		return head;
	}

	public TrainJobBody getBody() {
		return body;
	}

}

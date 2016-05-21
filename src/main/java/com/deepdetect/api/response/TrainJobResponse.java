package com.deepdetect.api.response;

import com.deepdetect.api.data.JobHead;
import com.deepdetect.api.data.TrainJobBody;

/**
 * Response to launching a blocking or asynchronous training job from a service
 */
public class TrainJobResponse extends DeepDetectResponse {

	private JobHead head;
	private TrainJobBody body;

	public JobHead getHead() {
		return head;
	}

	public TrainJobBody getBody() {
		return body;
	}

}

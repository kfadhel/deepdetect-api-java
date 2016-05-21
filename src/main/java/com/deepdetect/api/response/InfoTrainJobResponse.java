package com.deepdetect.api.response;

import com.deepdetect.api.data.JobHead;
import com.deepdetect.api.data.InfoTrainJobBody;

/**
 * Returns information on a training job running asynchronously
 */
public class InfoTrainJobResponse extends DeepDetectResponse {

	private JobHead head;
	private InfoTrainJobBody body;

	public JobHead getHead() {
		return head;
	}

	public InfoTrainJobBody getBody() {
		return body;
	}
}

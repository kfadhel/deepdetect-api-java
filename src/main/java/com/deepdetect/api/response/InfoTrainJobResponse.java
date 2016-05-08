package com.deepdetect.api.response;

import com.deepdetect.api.data.Head;
import com.deepdetect.api.data.InfoTrainJobBody;

/**
 * Returns information on a training job running asynchronously
 */
public class InfoTrainJobResponse extends DeepDetectResponse {

	private Head head;
	private InfoTrainJobBody body;

	public Head getHead() {
		return head;
	}

	public InfoTrainJobBody getBody() {
		return body;
	}
}

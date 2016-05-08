package com.deepdetect.api.response;

import com.deepdetect.api.data.InfoServiceBody;

/**
 * Returns information on an existing service
 */
public class InfoServiceResponse extends DeepDetectResponse {

	private InfoServiceBody body;

	public InfoServiceBody getBody() {
		return body;
	}
}

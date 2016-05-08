package com.deepdetect.api.response;

import com.deepdetect.api.data.InfoHead;

public class InfoResponse extends DeepDetectResponse {

	private InfoHead head;

	/**
	 * Info about the API
	 * 
	 * @return InfoHead
	 */
	public InfoHead getHead() {
		return head;
	}

}

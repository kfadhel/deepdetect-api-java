package com.deepdetect.api.response;

import com.deepdetect.api.data.DeleteTrainingJobHead;

public class DeleteTrainingJobResponse extends DeepDetectResponse {

	private DeleteTrainingJobHead head;

	public DeleteTrainingJobHead getHead() {
		return head;
	}

}

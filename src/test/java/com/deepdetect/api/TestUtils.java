package com.deepdetect.api;

import static com.google.common.base.Charsets.UTF_8;

import java.io.IOException;

import com.google.common.io.Resources;

public class TestUtils {

	public static final String GET_INFO_RESPONSE_FILE = "infoResponse.json";
	public static final String CREATE_SERVICE_RESPONSE_FILE = "createServiceResponse.json";
	public static final String GET_INFO_SERVICE_RESPONSE_SINGLE_JOB_FILE = "infoServiceSJResponse.json";
	public static final String GET_INFO_SERVICE_RESPONSE_MULTIPLE_JOBS_FILE = "infoServiceMJResponse.json";
	public static final String DELETE_SERVICE_RESPONSE_FILE = "deleteServiceResponse.json";
	public static final String TRAIN_JOB_RESPONSE_FILE = "trainJobResponse.json";
	public static final String GET_INFO_TRAIN_JOB_RESPONSE_FILE = "infoTrainJobResponse.json";
	public static final String DELETE_TRAIN_JOB_RESPONSE_FILE = "deleteTrainJobResponse.json";
	public static final String PREDICT_RESPONSE_FILE = "predictResponse.json";
	
	public static String getResourceAsString(String path) throws IOException {
		return Resources.toString(Resources.getResource(path), UTF_8);
	}
}

package com.deepdetect.api.data;

public class DeleteTrainingJobHead {

	double time;
	String status;
	String method;
	long job;

	public double getTime() {
		return time;
	}

	public String getStatus() {
		return status;
	}

	public String getMethod() {
		return method;
	}

	public long getJob() {
		return job;
	}

}

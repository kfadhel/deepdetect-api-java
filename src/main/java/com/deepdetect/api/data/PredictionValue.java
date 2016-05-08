package com.deepdetect.api.data;

import java.util.List;

public class PredictionValue {

	String uri;
	double loss;
	List<PredictionClass> classes;

	public String getUri() {
		return uri;
	}

	public double getLoss() {
		return loss;
	}

	public List<PredictionClass> getClasses() {
		return classes;
	}
}

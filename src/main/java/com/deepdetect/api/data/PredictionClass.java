package com.deepdetect.api.data;

public class PredictionClass {
	double prob;
	String cat;
	
	public double getProbability() {
		return prob;
	}
	public String getCategory() {
		return cat;
	}
}

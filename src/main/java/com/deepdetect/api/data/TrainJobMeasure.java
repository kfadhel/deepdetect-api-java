package com.deepdetect.api.data;

import com.google.gson.Gson;

public class TrainJobMeasure {

	double iteration;
	double train_loss;
	double mcll;
	double acc;

	public double getIteration() {
		return iteration;
	}

	public double getTrainLoss() {
		return train_loss;
	}

	public double getMcll() {
		return mcll;
	}

	public double getAcc() {
		return acc;
	}

	@Override
	public String toString() {
		return new Gson().toJson(this).toString();
	}
}

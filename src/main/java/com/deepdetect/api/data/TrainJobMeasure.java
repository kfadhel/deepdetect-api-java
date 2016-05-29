package com.deepdetect.api.data;

import com.google.gson.Gson;

public class TrainJobMeasure {

	long iteration;
	String train_loss;
	String mcll;
	String acc;

	public long getIteration() {
		return iteration;
	}

	public double getTrainLoss() {
		return Double.valueOf(train_loss);
	}

	public double getMcll() {
		return Double.valueOf(mcll);
	}

	public double getAcc() {
		return Double.valueOf(acc);
	}

	@Override
	public String toString() {
		return new Gson().toJson(this).toString();
	}
}

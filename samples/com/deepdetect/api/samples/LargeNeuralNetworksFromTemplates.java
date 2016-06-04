package com.deepdetect.api.samples;

import com.deepdetect.api.data.PredictionClass;
import com.deepdetect.api.enums.MLType;
import com.deepdetect.api.exceptions.DeepDetectException;
import com.deepdetect.api.request.CreateServiceRequest;
import com.deepdetect.api.request.DeleteServiceRequest;
import com.deepdetect.api.request.PredictRequest;
import com.deepdetect.api.response.CreateServiceResponse;
import com.deepdetect.api.response.PredictResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LargeNeuralNetworksFromTemplates {
	// you need to get the pre-trained model
	// look at here http://www.deepdetect.com/tutorials/imagenet-classifier/
	public static void main(String[] args) throws DeepDetectException, InterruptedException {

		if (args.length < 1) {
			System.err.println("you need to provide repository");
			System.err.println("use <repository>");
			System.exit(2);
		}

		String repository = args[0];

		String host = "http://localhost:8080";
		JsonParser parser = new JsonParser();

		// creating the service
		String serviceName = "imagenet";
		String description = "image classification service";
		String mllib = "caffe";

		JsonObject createSModel = parser.parse("{'templates':'../templates/caffe/','repository':'" + repository + "'}")
				.getAsJsonObject();
		JsonObject createSInput = parser.parse("{'connector':'image'}").getAsJsonObject();
		JsonObject createSOutput = parser.parse("{'template':'googlenet','nclasses':1000}").getAsJsonObject();

		CreateServiceResponse createSResponse = CreateServiceRequest.newCreateServiceRequest() //
				.baseURL(host) //
				.name(serviceName) //
				.description(description) //
				.type(MLType.SUPERVISED) //
				.mllib(mllib) //
				.input(createSInput) //
				.mllibParams(createSOutput) //
				.model(createSModel) //
				.build().process();

		System.out.println("CreateServiceResponse : " + createSResponse);

		Thread.sleep(1000);

		JsonObject params = parser.parse("{'input':{'width':224,'height':224},'output':{'best':3}}").getAsJsonObject();
		PredictResponse response = PredictRequest.newPredictRequest() //
				.baseURL(host) //
				.service(serviceName) //
				.data("http://i.ytimg.com/vi/0vxOhd4qlnA/maxresdefault.jpg") //
				.parameters(params) //
				.build().process();

		System.out.println(response);
		System.out.println("\npredictions");
		for (PredictionClass predictionClass : response.getBody().getPredictions().get(0).getClasses()) {
			System.out.println("\t- " + predictionClass.getCategory() + " : " + predictionClass.getProbability());
		}

		// delete service
		DeleteServiceRequest.newDeleteServiceRequest() //
				.baseURL(host) //
				.service(serviceName) //
				.build().process();

	}
}

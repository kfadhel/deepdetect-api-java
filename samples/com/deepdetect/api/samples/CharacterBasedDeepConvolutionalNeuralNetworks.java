package com.deepdetect.api.samples;

import java.util.Arrays;

import com.deepdetect.api.data.PredictionClass;
import com.deepdetect.api.data.PredictionValue;
import com.deepdetect.api.exceptions.DeepDetectException;
import com.deepdetect.api.request.CreateServiceRequest;
import com.deepdetect.api.request.DeleteServiceRequest;
import com.deepdetect.api.request.PredictRequest;
import com.deepdetect.api.response.CreateServiceResponse;
import com.deepdetect.api.response.PredictResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CharacterBasedDeepConvolutionalNeuralNetworks {

	// you need to set the model first as described here
	// http://www.deepdetect.com/applications/text_model/
	public static void main(String[] args) throws DeepDetectException, InterruptedException {

		if (args.length < 1) {
			System.err.println("you need to provide repository");
			System.exit(2);
		}

		String repository = args[0];
		String[] words = Arrays.copyOfRange(args, 1, args.length);

		String host = "http://localhost:8080";
		JsonParser parser = new JsonParser();

		// creating the service
		String serviceName = "sent_en";
		String description = "Arabic sentiment classification";
		String mllib = "caffe";

		JsonObject createSModel = parser.parse("{'templates':'../templates/caffe/','repository':'" + repository + "'}")
				.getAsJsonObject();
		JsonObject createSInput = parser
				.parse("{'connector':'txt','characters':True,'sequence':140,'alphabet':\"abcdefghijklmnopqrstuvwxyz0123456789,;.!?'\"}")
				.getAsJsonObject();
		JsonObject createSOutput = parser.parse("{'nclasses':2}").getAsJsonObject();

		CreateServiceResponse createSResponse = CreateServiceRequest.newCreateServiceRequest() //
				.baseURL(host) //
				.name(serviceName) //
				.description(description) //
				.mllib(mllib) //
				.input(createSInput) //
				.mllibParams(createSOutput) //
				.model(createSModel) //
				.build().process();

		System.out.println("CreateServiceResponse : " + createSResponse);

		Thread.sleep(1000);

		PredictResponse response = PredictRequest.newPredictRequest() //
				.baseURL(host) //
				.service(serviceName) //
				.data(words) //
				.build().process();

		System.out.println(response);
		System.out.println("\npredictions");
		for (int i = 0; i < response.getBody().getPredictions().size(); i++) {
			PredictionValue predictionValue = response.getBody().getPredictions().get(i);
			System.out.println(" * " + words[i]);
			for (PredictionClass predictionClass : predictionValue.getClasses()) {
				System.out.println("\t- " + predictionClass.getCategory() + " : " + predictionClass.getProbability());
			}
		}

		// delete service
		DeleteServiceRequest.newDeleteServiceRequest() //
				.baseURL(host) //
				.service(serviceName) //
				.build().process();

	}

}

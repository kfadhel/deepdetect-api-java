package com.deepdetect.api.samples;

import com.deepdetect.api.exceptions.DeepDetectException;
import com.deepdetect.api.request.CreateServiceRequest;
import com.deepdetect.api.request.DeleteServiceRequest;
import com.deepdetect.api.request.InfoTrainJobRequest;
import com.deepdetect.api.request.TrainJobRequest;
import com.deepdetect.api.response.CreateServiceResponse;
import com.deepdetect.api.response.InfoTrainJobResponse;
import com.deepdetect.api.response.TrainJobResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ConvolutionalNeuralNetwork {

	public static void main(String[] args) throws DeepDetectException, InterruptedException {

		if (args.length<2){
			System.err.println("you need to provide repository and data folder");
			System.err.println("use <repository> <datafolder>");
			System.exit(2);
		}
		
		String repository = args[0];
		String dataFolder = args[1];
		
		String host = "http://localhost:8080";
		JsonParser parser = new JsonParser();

		int height = 64;
		int width = 64;

		// creating the service
		String serviceName = "imageserv";
		String description = "image classification";
		String mllib = "caffe";

		JsonObject createSModel = parser
				.parse("{'templates':'../templates/caffe/','repository':'"+repository+"'}")
				.getAsJsonObject();
		JsonObject createSInput = parser.parse("{'connector':'image','width':" + width + ",'height':" + height + "}")
				.getAsJsonObject();
		JsonObject createSOutput = parser
				.parse("{'template':'convnet','nclasses':18,'layers':['1CR32','1CR64','1CR128','1024'],'dropout':0.2,'activation':'prelu','rotate':True,'mirror':True}")
				.getAsJsonObject();

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

		// training
		JsonObject trainInput = parser.parse("{'test_split':0.1,'shuffle':True}").getAsJsonObject();
		JsonObject trainMllib = parser
				.parse("{'gpu':True,'net':{'batch_size':128},'solver':{'test_interval':1000,'iterations':16000,'base_lr':0.001,'solver_type':'SGD'}}")
				.getAsJsonObject();
		JsonObject trainOutput = parser.parse("{'measure':['mcll','f1']}").getAsJsonObject();

		TrainJobResponse trainResponse = TrainJobRequest.newTrainJobRequest() //
				.baseURL(host) //
				.service(serviceName) //
				.async(true) //
				.input(trainInput) //
				.mllibParams(trainMllib) //
				.output(trainOutput) //
				.data(dataFolder) //
				.build().process();

		System.out.println("TrainJobResponse : " + trainResponse);

		// Job status
		Thread.sleep(2000);
		InfoTrainJobRequest infoRequest = InfoTrainJobRequest.newInfoTrainJobRequest() //
				.baseURL(host) //
				.service(serviceName) //
				.jobId(trainResponse.getHead().getJob()) //
				.timeout(10) //
				.build();
		
		InfoTrainJobResponse infoResponse;
		boolean training = true;
		while (training) {
			infoResponse = infoRequest.process();

			if (infoResponse.getHead().getStatus().equals("running")) {
				System.out.println(infoResponse.getBody().getMeasure());
			} else {
				System.out.println(infoResponse);
				training = false;
			}
			Thread.sleep(5000);
		}

		// delete service
		DeleteServiceRequest.newDeleteServiceRequest() //
				.baseURL(host) //
				.service(serviceName) //
				.clearType("lib") //
				.build().process();

	}
}

package com.deepdetect.api.samples;

import com.deepdetect.api.exceptions.DeepDetectException;
import com.deepdetect.api.request.CreateServiceRequest;
import com.deepdetect.api.request.DeleteServiceRequest;
import com.deepdetect.api.request.InfoTrainJobRequest;
import com.deepdetect.api.request.TrainJobRequest;
import com.deepdetect.api.response.CreateServiceResponse;
import com.deepdetect.api.response.InfoTrainJobResponse;
import com.deepdetect.api.response.TrainJobResponse;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MultilayerPerceptron {

	public static void main(String[] args) throws DeepDetectException, InterruptedException {

		String host = "http://localhost:8080";
		JsonParser parser = new JsonParser();

		// creating the service
		String serviceName = "covert";
		String description = "forest classification service";
		String mllib = "caffe";

		JsonArray layers = new JsonArray();
		layers.add(150);
		layers.add(150);
		layers.add(150);

		JsonObject createSModel = parser
				.parse("{'templates':'../templates/caffe/','repository':'/home/kml/spool/ddmodels/covert'}")
				.getAsJsonObject();
		JsonObject createSInput = parser.parse("{'connector':'csv'}").getAsJsonObject();
		JsonObject createSOutput = parser.parse("{'template':'mlp','nclasses':7,'activation':'prelu','dropout':0.2}")
				.getAsJsonObject();
		createSOutput.add("layers", layers);

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
		JsonObject trainInput = parser
				.parse("{'shuffle':True,'test_split':0.1,'id':'Id','label':'Cover_Type','separator':',','scale':True,'label_offset':-1}")
				.getAsJsonObject();
		JsonObject trainMllib = parser
				.parse("{'gpu':True,'solver':{'iterations':10000,'test_interval':100,'base_lr':0.05},'net':{'batch_size':500}}")
				.getAsJsonObject();
		JsonObject trainOutput = parser.parse("{'measure':['f1','mcll']}").getAsJsonObject();

		TrainJobResponse trainResponse = TrainJobRequest.newTrainJobRequest() //
				.baseURL(host) //
				.service(serviceName) //
				.async(true) //
				.input(trainInput) //
				.mllibParams(trainMllib) //
				.output(trainOutput) //
				.data("/home/kml/spool/ddmodels/covert/train.csv") //
				.build().process();

		System.out.println("TrainJobResponse : " + trainResponse);

		// Job status
		Thread.sleep(2000);
		InfoTrainJobResponse infoResponse;
		boolean training = true;
		while (training) {
			infoResponse = InfoTrainJobRequest.newInfoTrainJobRequest() //
					.baseURL(host) //
					.service(serviceName) //
					.jobId(trainResponse.getHead().getJob()) //
					.timeout(10) //
					.build().process();

			if (infoResponse.getHead().getStatus().equals("running")) {
				System.out.println(infoResponse.getBody().getMeasure());
			} else {
				System.out.println(infoResponse);
				training = false;
			}
		}

		// delete service
		DeleteServiceRequest.newDeleteServiceRequest() //
				.baseURL(host) //
				.service(serviceName) //
				.clearType("lib") //
				.build().process();
	}

}

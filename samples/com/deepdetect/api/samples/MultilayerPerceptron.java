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

public class MultilayerPerceptron {

	public static void main(String[] args) throws DeepDetectException, InterruptedException {

		if (args.length < 2) {
			System.err.println("you need to provide repository and data folder");
			System.err.println("use <repository> <datafile>");
			System.exit(2);
		}

		String repository = args[0];
		String dataFile = args[1];

		String host = "http://localhost:8080";

		// creating the service
		String serviceName = "covert";
		String description = "forest classification service";
		String mllib = "caffe";

		JsonArray layers = new JsonArray();
		layers.add(150);
		layers.add(150);
		layers.add(150);

		String createSModel = "{'templates':'../templates/caffe/','repository':'" + repository + "'}";
		String createSInput = "{'connector':'csv'}";
		String createSOutput = "{'template':'mlp','nclasses':7,'activation':'prelu','dropout':0.2,'layers':[150,150,150]}";

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
		String trainInput = "{'shuffle':True,'test_split':0.1,'id':'Id','label':'Cover_Type','separator':',','scale':True,'label_offset':-1}";
		String trainMllib = "{'gpu':True,'solver':{'iterations':10000,'test_interval':100,'base_lr':0.05},'net':{'batch_size':500}}";
		String trainOutput = "{'measure':['f1','mcll']}";

		TrainJobResponse trainResponse = TrainJobRequest.newTrainJobRequest() //
				.baseURL(host) //
				.service(serviceName) //
				.async(true) //
				.input(trainInput) //
				.mllibParams(trainMllib) //
				.output(trainOutput) //
				.data(dataFile) //
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
